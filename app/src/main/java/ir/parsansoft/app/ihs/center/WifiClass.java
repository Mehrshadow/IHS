package ir.parsansoft.app.ihs.center;

public class WifiClass {
    public String ssid;
    public String psk;
    public String mac;
    public String type;
    public int    level;

    public WifiClass(String name, String bssid, String type, int level) {
        //super();
        this.ssid = name;
        this.mac = bssid;
        this.type = type;
        this.level = level;
    }
}
