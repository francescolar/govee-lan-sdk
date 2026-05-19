package io.github.govee.lan;

import io.github.govee.lan.model.DeviceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoveeControllerTest {

    private RecordingUdpClient recorder;
    private GoveeController controller;

    @BeforeEach
    void setUp() {
        recorder = new RecordingUdpClient();
        controller = new GoveeController("192.168.1.42", recorder);
    }

    @Test
    void turnOnSendsCorrectJson() {
        controller.turn(true);
        assertEquals(1, recorder.sent.size());
        assertEquals("{\"msg\":{\"cmd\":\"turn\",\"data\":{\"value\":1}}}", recorder.sent.get(0).json);
        assertEquals("192.168.1.42", recorder.sent.get(0).ip);
        assertEquals(GoveeUdpClient.CONTROL_PORT, recorder.sent.get(0).port);
    }

    @Test
    void turnOffSendsCorrectJson() {
        controller.turn(false);
        assertEquals("{\"msg\":{\"cmd\":\"turn\",\"data\":{\"value\":0}}}", recorder.sent.get(0).json);
    }

    @Test
    void setBrightnessSendsCorrectJson() {
        controller.setBrightness(75);
        assertEquals("{\"msg\":{\"cmd\":\"brightness\",\"data\":{\"value\":75}}}", recorder.sent.get(0).json);
    }

    @Test
    void setColorSendsCorrectJson() {
        controller.setColor(255, 128, 0);
        assertEquals(
            "{\"msg\":{\"cmd\":\"colorwc\",\"data\":{\"color\":{\"r\":255,\"g\":128,\"b\":0},\"colorTemInKelvin\":0}}}",
            recorder.sent.get(0).json
        );
    }

    @Test
    void setColorTempSendsCorrectJson() {
        controller.setColorTemp(6500);
        assertEquals(
            "{\"msg\":{\"cmd\":\"colorwc\",\"data\":{\"color\":{\"r\":0,\"g\":0,\"b\":0},\"colorTemInKelvin\":6500}}}",
            recorder.sent.get(0).json
        );
    }

    @Test
    void getStatusParsesResponse() {
        recorder.responseJson = "{\"msg\":{\"cmd\":\"devStatus\",\"data\":{\"onOff\":1,\"brightness\":80,"
                + "\"color\":{\"r\":255,\"g\":0,\"b\":0},\"colorTemInKelvin\":7200}}}";

        DeviceStatus status = controller.getStatus();

        assertTrue(status.isOn());
        assertEquals(80, status.getBrightness());
        assertEquals(255, status.getR());
        assertEquals(0, status.getG());
        assertEquals(0, status.getB());
        assertEquals(7200, status.getColorTemInKelvin());
    }

    // ── minimal stub that records outgoing calls ───────────────────────────

    static class RecordingUdpClient extends GoveeUdpClient {

        static class SentPacket {
            final String ip; final int port; final String json;
            SentPacket(String ip, int port, String json) { this.ip = ip; this.port = port; this.json = json; }
        }

        final List<SentPacket> sent = new ArrayList<>();
        String responseJson = "";

        @Override
        public void send(String ip, int port, String json) {
            sent.add(new SentPacket(ip, port, json));
        }

        @Override
        public String sendAndReceive(String ip, int port, String json, int timeoutMs) {
            sent.add(new SentPacket(ip, port, json));
            return responseJson;
        }
    }
}
