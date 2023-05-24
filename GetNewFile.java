import java.util.HashSet;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import FnsEnginie.FnsDownload;
import settings.Settings;

public class GetNewFile {
    public static Set<String> getFileDifference(String baseFileList, String newFileList) throws IOException {
        Set<String> baseFiles = new HashSet<>(Files.readAllLines(Paths.get(baseFileList)));
        Set<String> newFiles = new HashSet<>(Files.readAllLines(Paths.get(newFileList)));
        newFiles.removeAll(baseFiles);
        return newFiles;
    }

    public static void downloadDifferences(FnsDownload fnsEngine, Set<String> filesToDownload, String targetDir) {
        for (String file : filesToDownload) {
            fnsEngine.downloadFile(file, targetDir);
        }
    }

    public static void downloadNew(FnsDownload fnsEngine, String baseFile, String newFile, String targetDir) throws IOException {
        Set<String> delta = getFileDifference(baseFile, newFile);
        System.out.println("To download: " + delta);
        downloadDifferences(fnsEngine, delta, targetDir);
        System.out.println("Done to: " + Settings.FNS_dir_target);
        if (Files.isRegularFile(Paths.get(baseFile)) && Files.isRegularFile(Paths.get(newFile))) {
            Files.delete(Paths.get(baseFile));
            Files.delete(Paths.get(newFile));
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("GetNewFile started");

        FnsDownload fnsUL = new FnsDownload(Settings.proxies, Settings.headers, Settings.ulcert,
                Settings.ulkey, Settings.url, Settings.ulstart_dir, Settings.ex_dir);
        System.out.println("EGRUL engine init done");

        FnsDownload fnsIP = new FnsDownload(Settings.proxies, Settings.headers, Settings.ipcert,
                Settings.ipkey, Settings.url, Settings.ipstart_dir, Settings.ex_dir);
        System.out.println("EGRIP engine init done");

        fnsUL.fileListToFile("EGRUL_last.txt");
        System.out.println("EGRUL_last.txt done");

        fnsIP.fileListToFile("EGRIP_last.txt");
        System.out.println("EGRIP_last.txt done");

        downloadNew(fnsUL, "EGRUL.txt", "EGRUL_last.txt", Settings.FNS_dir_target);
        downloadNew(fnsIP, "EGRIP.txt", "EGRIP_last.txt", Settings.FNS_dir_target);

        System.out.println("ALL done");
    }
}
