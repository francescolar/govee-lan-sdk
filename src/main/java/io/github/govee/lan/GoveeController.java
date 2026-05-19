package io.github.govee.lan;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.govee.lan.command.CommandFactory;
import io.github.govee.lan.model.DeviceStatus;

public class GoveeController {

    private static final int DEFAULT_STATUS_TIMEOUT_MS = 3000;

    private final String ip;
    private final GoveeUdpClient udpClient;

    public GoveeController(String ip) {
        this(ip, new GoveeUdpClient());
    }

    GoveeController(String ip, GoveeUdpClient udpClient) {
        this.ip = ip;
        this.udpClient = udpClient;
    }

    public void turn(boolean on) {
        send(CommandFactory.turn(on));
    }

    /** @param value 1–100 */
    public void setBrightness(int value) {
        send(CommandFactory.brightness(value));
    }

    /** Pure RGB color (r, g, b each 0–255). */
    public void setColor(int r, int g, int b) {
        send(CommandFactory.color(r, g, b));
    }

    /** Color temperature in Kelvin (2000–9000). */
    public void setColorTemp(int kelvin) {
        send(CommandFactory.colorTemp(kelvin));
    }

    /**
     * Queries the device status and returns the parsed response.
     * Blocks until the device replies or the default timeout elapses.
     */
    public DeviceStatus getStatus() {
        return getStatus(DEFAULT_STATUS_TIMEOUT_MS);
    }

    public DeviceStatus getStatus(int timeoutMs) {
        String responseJson = udpClient.sendAndReceive(
                ip, GoveeUdpClient.CONTROL_PORT, CommandFactory.devStatus(), timeoutMs);
        return parseStatusResponse(responseJson);
    }

    public String getIp() {
        return ip;
    }

    private void send(String json) {
        udpClient.send(ip, GoveeUdpClient.CONTROL_PORT, json);
    }

    private static DeviceStatus parseStatusResponse(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonObject data = root.getAsJsonObject("msg").getAsJsonObject("data");

        boolean on = data.get("onOff").getAsInt() == 1;
        int brightness = data.get("brightness").getAsInt();
        JsonObject color = data.getAsJsonObject("color");
        int r = color.get("r").getAsInt();
        int g = color.get("g").getAsInt();
        int b = color.get("b").getAsInt();
        int colorTem = data.has("colorTemInKelvin") ? data.get("colorTemInKelvin").getAsInt() : 0;

        return new DeviceStatus(on, brightness, r, g, b, colorTem);
    }
}
