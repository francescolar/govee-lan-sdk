package io.github.govee.lan.model;

public class GoveeDevice {

    private final String ip;
    private final String device;
    private final String sku;
    private final String bleVersionHard;
    private final String bleVersionSoft;
    private final String wifiVersionHard;
    private final String wifiVersionSoft;

    public GoveeDevice(String ip, String device, String sku,
                       String bleVersionHard, String bleVersionSoft,
                       String wifiVersionHard, String wifiVersionSoft) {
        this.ip = ip;
        this.device = device;
        this.sku = sku;
        this.bleVersionHard = bleVersionHard;
        this.bleVersionSoft = bleVersionSoft;
        this.wifiVersionHard = wifiVersionHard;
        this.wifiVersionSoft = wifiVersionSoft;
    }

    public String getIp() { return ip; }
    public String getDevice() { return device; }
    public String getSku() { return sku; }
    public String getBleVersionHard() { return bleVersionHard; }
    public String getBleVersionSoft() { return bleVersionSoft; }
    public String getWifiVersionHard() { return wifiVersionHard; }
    public String getWifiVersionSoft() { return wifiVersionSoft; }

    @Override
    public String toString() {
        return "GoveeDevice{ip='" + ip + "', device='" + device + "', sku='" + sku + "'}";
    }
}
