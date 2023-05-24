public class InitFileList {
    public static void main(String[] args) {
        System.out.println("InitFileList started");

        FnsEngine fnsUL = new FnsEngine(settings.proxies, settings.headers, settings.ulcert,
                settings.ulkey, settings.url, settings.ulstart_dir, settings.ex_dir);
        System.out.println("EGRUL engine init done");

        FnsEngine fnsIP = new FnsEngine(settings.proxies, settings.headers, settings.ipcert,
                settings.ipkey, settings.url, settings.ipstart_dir, settings.ex_dir);
        System.out.println("EGRIP engine init done");

        fnsUL.fileListToFile("EGRUL.txt");
        System.out.println("EGRUL.txt done");

        fnsIP.fileListToFile("EGRIP.txt");
        System.out.println("EGRIP.txt done");
    }
}
