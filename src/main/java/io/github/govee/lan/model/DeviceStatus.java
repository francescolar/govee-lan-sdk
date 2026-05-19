package io.github.govee.lan.model;

public class DeviceStatus {

    private final boolean on;
    private final int brightness;
    private final int r;
    private final int g;
    private final int b;
    private final int colorTemInKelvin;

    public DeviceStatus(boolean on, int brightness, int r, int g, int b, int colorTemInKelvin) {
        this.on = on;
        this.brightness = brightness;
        this.r = r;
        this.g = g;
        this.b = b;
        this.colorTemInKelvin = colorTemInKelvin;
    }

    public boolean isOn() { return on; }
    public int getBrightness() { return brightness; }
    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }
    public int getColorTemInKelvin() { return colorTemInKelvin; }

    @Override
    public String toString() {
        return "DeviceStatus{on=" + on + ", brightness=" + brightness
                + ", rgb=(" + r + "," + g + "," + b + ")"
                + ", colorTem=" + colorTemInKelvin + "K}";
    }
}
