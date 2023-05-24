import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FnsDownload {
    private String proxies;
    private String headers;
    private String certStr;
    private String keyStr;
    private String url;
    private String startDir;
    private List<String> exDir;

    public FnsDownload(String proxies, String headers, String certStr, String keyStr, String url, String startDir,
            List<String> exDir) {
        this.proxies = proxies;
        this.headers = headers;
        this.certStr = certStr;
        this.keyStr = keyStr;
        this.url = url;
        this.startDir = startDir;
        this.exDir = exDir;
        System.setProperty("jsse.enableSNIExtension", "false");
    }

    private List<String> hrefParsing(String dir) throws IOException {
        Document response = Jsoup.connect(url + dir).proxy(proxies).ignoreContentType(true)
                .header("User-Agent", headers).validateTLSCertificates(false).get();
        Elements links = response.getElementsByClass("clearfix");
        List<String> hrefLinks = new ArrayList<>();
        for (Element link : links) {
            String href = link.attr("href");
            if (href.startsWith("?") || href.toLowerCase().endsWith(".zip")) {
                hrefLinks.add(href);
            }
        }
        return hrefLinks;
    }

    private List<String> getFileList(String dir) throws IOException {
        List<String> subFolders = hrefParsing(dir);
        List<String> fileList = new ArrayList<>();
        for (String directory : subFolders) {
            int patternCount = 0;
            for (String flt : exDir) {
                if (directory.contains(flt)) {
                    patternCount++;
                    break;
                }
            }
            if (patternCount == 0) {
                fileList.addAll(hrefParsing(directory));
            }
        }
        return fileList;
    }

    public void fileListToFile(String filename) throws IOException {
        List<String> fileList = getFileList(startDir);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String file : fileList) {
                writer.write(file + "\n");
            }
        }
    }

    private String getFileName(String string) {
        return string.substring(string.lastIndexOf('/') + 1);
    }

    public void downloadFile(String fileToDownload, String targetDir) throws IOException {
        byte[] response = Jsoup.connect(url + fileToDownload).proxy(proxies).ignoreContentType(true)
                .header("User-Agent", headers).validateTLSCertificates(false).execute().bodyAsBytes();
        Files.write(Paths.get(targetDir + getFileName(fileToDownload)), response);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("To local test");
    }
}
