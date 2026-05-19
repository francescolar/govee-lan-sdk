package io.github.govee.lan;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.govee.lan.command.CommandFactory;
import io.github.govee.lan.model.GoveeDevice;

import java.util.ArrayList;
import java.util.List;

public final class GoveeDiscovery {

    private GoveeDiscovery() {}

    /**
     * Broadcasts a scan request to the multicast group and collects responses.
     *
     * @param timeoutMs how long to wait for device responses
     * @return list of discovered devices
     */
    public static List<GoveeDevice> scan(int timeoutMs) {
        GoveeUdpClient client = new GoveeUdpClient();
        String scanJson = CommandFactory.scan();
        List<String> rawResponses = client.multicastSendAndReceive(scanJson, timeoutMs);

        List<GoveeDevice> devices = new ArrayList<>();
        for (String raw : rawResponses) {
            try {
                GoveeDevice device = parseScanResponse(raw);
                if (device != null) {
                    devices.add(device);
                }
            } catch (Exception ignored) {
                // skip malformed responses
            }
        }
        return devices;
    }

    private static GoveeDevice parseScanResponse(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonObject msg = root.getAsJsonObject("msg");
        if (msg == null || !"scan".equals(msg.get("cmd").getAsString())) {
            return null;
        }
        JsonObject data = msg.getAsJsonObject("data");
        return new GoveeDevice(
                getString(data, "ip"),
                getString(data, "device"),
                getString(data, "sku"),
                getString(data, "bleVersionHard"),
                getString(data, "bleVersionSoft"),
                getString(data, "wifiVersionHard"),
                getString(data, "wifiVersionSoft")
        );
    }

    private static String getString(JsonObject obj, String key) {
        return obj.has(key) ? obj.get(key).getAsString() : "";
    }
}
