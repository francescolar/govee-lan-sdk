package io.github.govee.lan;

import io.github.govee.lan.exception.GoveeException;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class GoveeUdpClient {

    public static final int CONTROL_PORT  = 4003;
    public static final int SCAN_PORT     = 4001;
    public static final int RESPONSE_PORT = 4002;
    public static final String MULTICAST  = "239.255.255.250";

    private static final int BUFFER_SIZE = 1024;

    /**
     * Fire-and-forget UDP send. Used for all control commands (turn, brightness, colorwc).
     */
    public void send(String ip, int port, String json) {
        byte[] payload = json.getBytes(StandardCharsets.UTF_8);
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(payload, payload.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            throw new GoveeException("Failed to send UDP command to " + ip + ":" + port, e);
        }
    }

    /**
     * Sends a UDP packet and waits for a response on RESPONSE_PORT.
     * Used for devStatus queries.
     */
    public String sendAndReceive(String ip, int port, String json, int timeoutMs) {
        byte[] payload = json.getBytes(StandardCharsets.UTF_8);
        try (DatagramSocket socket = new DatagramSocket(RESPONSE_PORT)) {
            socket.setSoTimeout(timeoutMs);
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket outPacket = new DatagramPacket(payload, payload.length, address, port);
            socket.send(outPacket);

            byte[] buf = new byte[BUFFER_SIZE];
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            socket.receive(inPacket);
            return new String(inPacket.getData(), 0, inPacket.getLength(), StandardCharsets.UTF_8);
        } catch (SocketTimeoutException e) {
            throw new GoveeException("Timeout waiting for response from " + ip + " after " + timeoutMs + "ms", e);
        } catch (IOException e) {
            throw new GoveeException("UDP send/receive failed for " + ip + ":" + port, e);
        }
    }

    /**
     * Sends a multicast scan request and collects all responses within timeoutMs.
     * Returns raw JSON strings, one per responding device.
     */
    public java.util.List<String> multicastSendAndReceive(String json, int timeoutMs) {
        byte[] payload = json.getBytes(StandardCharsets.UTF_8);
        java.util.List<String> responses = new java.util.ArrayList<>();
        try (MulticastSocket socket = new MulticastSocket(RESPONSE_PORT)) {
            socket.setSoTimeout(timeoutMs);
            InetAddress group = InetAddress.getByName(MULTICAST);
            socket.joinGroup(group);

            InetAddress target = InetAddress.getByName(MULTICAST);
            DatagramPacket outPacket = new DatagramPacket(payload, payload.length, target, SCAN_PORT);
            socket.send(outPacket);

            byte[] buf = new byte[BUFFER_SIZE];
            while (true) {
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(inPacket);
                    responses.add(new String(inPacket.getData(), 0, inPacket.getLength(), StandardCharsets.UTF_8));
                } catch (SocketTimeoutException e) {
                    break;
                }
            }
            socket.leaveGroup(group);
        } catch (IOException e) {
            throw new GoveeException("Multicast scan failed", e);
        }
        return responses;
    }
}
