public class Settings {
    public static final String url = "https://ftp.egrul.nalog.ru/";
    public static final String ulcert = "$186303_cert.pem";
    public static final String ulkey = "$186303_key.pem";
    public static final String ulstart_dir = "?dir=EGRUL_406";
    public static final String ipcert = "$186304_cert.pem";
    public static final String ipkey = "$186304_key.pem";
    public static final String ipstart_dir = "?dir=EGRUL_405";
    public static final String FNS_dir_local = "../../SrcFiles/FNS/";

    public static final Map<String, String> headers = new HashMap<>();
    static {
        headers.put("User-Agent", "Mozilla/5.0");
    }

    public static final Set<String> ex_dir = new HashSet<>();
    static {
        ex_dir.add("_FULL");
        ex_dir.add(".2022");
    }

    // For local testing
    // public static final String username = "";
    // public static final String password = "";
    // public static final Map<String, String> proxies = new HashMap<>();
    // static {
    //     proxies.put("http", "http://" + username + ":" + password + "@url_Address:8080");
    //     proxies.put("https", "http://" + username + ":" + password + "@url_Address:8080");
    // }

    public static final Map<String, String> proxies = new HashMap<>();
}
