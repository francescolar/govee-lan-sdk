package io.github.govee.lan.command;

import com.google.gson.Gson;

public final class CommandFactory {

    private static final Gson GSON = new Gson();

    private CommandFactory() {}

    public static String turn(boolean on) {
        TurnData data = new TurnData(on ? 1 : 0);
        return serialize("turn", data);
    }

    public static String brightness(int value) {
        if (value < 1 || value > 100) {
            throw new IllegalArgumentException("Brightness must be between 1 and 100, got: " + value);
        }
        return serialize("brightness", new ValueData(value));
    }

    /** Pure RGB color: colorTemInKelvin is set to 0 so the device uses only RGB. */
    public static String color(int r, int g, int b) {
        validateRgb(r, g, b);
        return serialize("colorwc", new ColorwcData(new ColorRgb(r, g, b), 0));
    }

    /** Color temperature mode (2000–9000 K). RGB is set to black; device converts internally. */
    public static String colorTemp(int kelvin) {
        if (kelvin < 2000 || kelvin > 9000) {
            throw new IllegalArgumentException("Color temperature must be between 2000 and 9000 K, got: " + kelvin);
        }
        return serialize("colorwc", new ColorwcData(new ColorRgb(0, 0, 0), kelvin));
    }

    public static String devStatus() {
        return serialize("devStatus", new EmptyData());
    }

    public static String scan() {
        return serialize("scan", new ScanData("reserve"));
    }

    private static String serialize(String cmd, Object data) {
        return GSON.toJson(new GoveeMessage(new GoveeCmd(cmd, data)));
    }

    private static void validateRgb(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException(
                    "RGB values must be 0–255, got: r=" + r + " g=" + g + " b=" + b);
        }
    }

    // ── private data DTOs ──────────────────────────────────────────────────

    private static class TurnData {
        final int value;
        TurnData(int value) { this.value = value; }
    }

    private static class ValueData {
        final int value;
        ValueData(int value) { this.value = value; }
    }

    private static class ColorRgb {
        final int r, g, b;
        ColorRgb(int r, int g, int b) { this.r = r; this.g = g; this.b = b; }
    }

    private static class ColorwcData {
        final ColorRgb color;
        final int colorTemInKelvin;
        ColorwcData(ColorRgb color, int colorTemInKelvin) {
            this.color = color;
            this.colorTemInKelvin = colorTemInKelvin;
        }
    }

    private static class EmptyData {}

    private static class ScanData {
        final String account_topic;
        ScanData(String account_topic) { this.account_topic = account_topic; }
    }
}
