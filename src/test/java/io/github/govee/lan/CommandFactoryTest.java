package io.github.govee.lan;

import io.github.govee.lan.command.CommandFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandFactoryTest {

    @Test
    void turnOn() {
        assertEquals("{\"msg\":{\"cmd\":\"turn\",\"data\":{\"value\":1}}}", CommandFactory.turn(true));
    }

    @Test
    void turnOff() {
        assertEquals("{\"msg\":{\"cmd\":\"turn\",\"data\":{\"value\":0}}}", CommandFactory.turn(false));
    }

    @Test
    void brightness() {
        assertEquals("{\"msg\":{\"cmd\":\"brightness\",\"data\":{\"value\":50}}}", CommandFactory.brightness(50));
    }

    @Test
    void brightnessMin() {
        assertEquals("{\"msg\":{\"cmd\":\"brightness\",\"data\":{\"value\":1}}}", CommandFactory.brightness(1));
    }

    @Test
    void brightnessMax() {
        assertEquals("{\"msg\":{\"cmd\":\"brightness\",\"data\":{\"value\":100}}}", CommandFactory.brightness(100));
    }

    @Test
    void brightnessOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.brightness(0));
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.brightness(101));
    }

    @Test
    void color() {
        String json = CommandFactory.color(255, 0, 0);
        assertEquals("{\"msg\":{\"cmd\":\"colorwc\",\"data\":{\"color\":{\"r\":255,\"g\":0,\"b\":0},\"colorTemInKelvin\":0}}}", json);
    }

    @Test
    void colorInvalidRgb() {
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.color(256, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.color(0, -1, 0));
    }

    @Test
    void colorTemp() {
        String json = CommandFactory.colorTemp(4000);
        assertEquals("{\"msg\":{\"cmd\":\"colorwc\",\"data\":{\"color\":{\"r\":0,\"g\":0,\"b\":0},\"colorTemInKelvin\":4000}}}", json);
    }

    @Test
    void colorTempOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.colorTemp(1999));
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.colorTemp(9001));
    }

    @Test
    void devStatus() {
        assertEquals("{\"msg\":{\"cmd\":\"devStatus\",\"data\":{}}}", CommandFactory.devStatus());
    }

    @Test
    void scan() {
        assertEquals("{\"msg\":{\"cmd\":\"scan\",\"data\":{\"account_topic\":\"reserve\"}}}", CommandFactory.scan());
    }
}
