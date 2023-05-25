import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class FnsDownload {
    private String url;
    private String certStr;
    private String keyStr;
    private String startDir;
    private Set<String> exDir;

    public FnsDownload(String url, String certStr, String keyStr, String startDir, Set<String> exDir) {
        this.url = url;
        this.certStr = certStr;
        this.keyStr = keyStr;
        this.startDir = startDir;
        this.exDir = exDir;
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    private Elements getHrefLinks(String dir) throws IOException {
        Document doc = Jsoup.connect(url + dir)
                .header("User-Agent", "Mozilla/5.0")
                .sslSocketFactory(Utils.getSslSocketFactory(certStr, keyStr))
                .get();
        Elements hrefLinks = doc.select(".clearfix[href^='?'], .clearfix[href$='.zip']");
        return hrefLinks;
    }

    public Set<String> getFileList(String dir) throws IOException {
        Set<String> fileSet = new HashSet<>();
        Elements subFolders = getHrefLinks(dir);
        for (Element folder : subFolders) {
            int patternCount = 0;
            for (String filter : exDir) {
                if (folder.attr("href").contains(filter)) {
                    patternCount++;
                    break;
                }
            }
            if (patternCount == 0) {
                fileSet.addAll(getHrefLinks(folder.attr("href")).eachAttr("href"));
            }
        }
        return fileSet;
    }

    public void fileListToFile(String filename) throws IOException {
        Set<String> fileList = getFileList(startDir);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String file : fileList) {
                writer.write(file + "\n");
            }
        }
    }

    public String getFileName(String string) {
        return string.substring(string.lastIndexOf('/') + 1);
    }

    public void downloadFile(String fileToDownload, String targetDir) throws IOException {
        byte[] fileBytes = Jsoup.connect(url + fileToDownload)
                .header("User-Agent", "Mozilla/5.0")
                .sslSocketFactory(Utils.getSslSocketFactory(certStr, keyStr))
                .ignoreContentType(true)
                .execute()
                .bodyAsBytes();
        Files.write(Paths.get(targetDir + getFileName(fileToDownload)), fileBytes);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("To local test");

        // Settings
        String url = "https://ftp.egrul.nalog.ru/";
        String ulcert = "$186303_cert.pem";
        String ulkey = "$186303_key.pem";
        String ulstartDir = "?dir=EGRUL_406";
        String ipcert = "$186304_cert.pem";
        String ipkey = "$186304_key.pem";
        String ipstartDir = "?dir=EGRUL_405";
        String fnsDirLocal = "../../SrcFiles/FNS/";

        Set<String> exDir = new HashSet<>();
        exDir.add("_FULL");
        exDir.add(".2022");

      
      FnsDownload fnsUL = new FnsDownload(url, ulcert, ulkey, ulstartDir, exDir);
        System.out.println("EGRUL engine init done");
        FnsDownload fnsIP = new FnsDownload(url, ipcert, ipkey, ipstartDir, exDir);
        System.out.println("EGRIP engine init done");
        fnsUL.fileListToFile("EGRUL_last.txt");
        System.out.println("EGRUL_last.txt done");
        fnsIP.fileListToFile("EGRIP_last.txt");
        System.out.println("EGRIP_last.txt done");
        downloadNew(fnsUL, "EGRUL.txt", "EGRUL_last.txt", fnsDirLocal);
        downloadNew(fnsIP, "EGRIP.txt", "EGRIP_last.txt", fnsDirLocal);
        System.out.println("ALL done");
    }

    public static void downloadNew(FnsDownload fnsEngine, String baseFile, String newFile, String targetDir) throws IOException {
        Set<String> delta = getFileDiff(baseFile, newFile);
        System.out.println("to download: " + delta);
        downloadDiff(fnsEngine, delta, targetDir);
        System.out.println("done to: " + targetDir);
        if (Files.exists(Paths.get(baseFile))) {
            Files.delete(Paths.get(baseFile));
        }
        if (Files.exists(Paths.get(newFile))) {
            Files.delete(Paths.get(newFile));
        }
    }

    public static Set<String> getFileDiff(String baseFileList, String newFileList) throws IOException {
        Set<String> baseSet = Files.readAllLines(Paths.get(baseFileList)).stream()
                .map(String::trim)
                .collect(Collectors.toSet());
        Set<String> newSet = Files.readAllLines(Paths.get(newFileList)).stream()
                .map(String::trim)
                .collect(Collectors.toSet());
        return newSet.stream().filter(s -> !baseSet.contains(s)).collect(Collectors.toSet());
    }

    public static void downloadDiff(FnsDownload fnsEngine, Set<String> setToDownload, String targetDir) throws IOException {
        for (String file : setToDownload) {
            fnsEngine.downloadFile(file, targetDir);
        }
    }
}
