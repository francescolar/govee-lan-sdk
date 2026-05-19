# govee-lan-sdk

A lightweight Java library to control Govee smart devices over the local network using Govee's official LAN UDP API.

No cloud, no API key, no rate limits. Just direct UDP communication with the bulbs and strips on your WiFi.

## Requirements

- Java 11 or newer
- A Govee device with LAN Control enabled in the Govee Home app (not all models support it — official compatibility list at https://app-h5.govee.com/user-manual/wlan-guide)
- Device on the same LAN as the machine running your code

## Installation

```xml
<dependency>
    <groupId>com.laaer</groupId>
    <artifactId>govee-lan-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### Controlling a device directly

If you already know the IP:

```java
GoveeController ctrl = new GoveeController("192.168.1.42");

ctrl.turn(true);               // turn on
ctrl.turn(false);              // turn off
ctrl.setBrightness(50);        // 1-100
ctrl.setColor(255, 100, 0);    // RGB
ctrl.setColorTemp(4000);       // Kelvin, 2000-9000

DeviceStatus status = ctrl.getStatus();
System.out.println(status.isOn());
System.out.println(status.getBrightness());
```

### Discovering devices on the network

```java
List<GoveeDevice> devices = GoveeDiscovery.scan(3000); // 3s timeout

for (GoveeDevice d : devices) {
    System.out.println(d.getIp() + " - " + d.getSku());

    GoveeController ctrl = new GoveeController(d.getIp());
    ctrl.turn(true);
}
```

### DeviceStatus fields

```java
DeviceStatus s = ctrl.getStatus();

s.isOn()               // boolean
s.getBrightness()      // int, 1-100
s.getR()               // int, 0-255
s.getG()               // int, 0-255
s.getB()               // int, 0-255
s.getColorTemInKelvin() // int, 2000-9000 (0 if in RGB mode)
```

## Protocol notes

- Discovery: multicast UDP to `239.255.255.250:4001`, responses come back on port `4002`
- Control commands: unicast UDP to `device-ip:4003`
- Status responses: unicast UDP back to port `4002`
- Color and color temperature share the same `colorwc` command — `colorTemInKelvin=0` means pure RGB mode

All of this is based on Govee's official WLAN guide: https://app-h5.govee.com/user-manual/wlan-guide

## License

MIT
