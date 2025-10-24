package org.example.util;


import org.example.model.Device;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private static final Pattern MAC_PATTERN = Pattern.compile(
            "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
    );

    public static boolean isValidIpAddress(String ip) {
        return ip != null && IP_PATTERN.matcher(ip).matches();
    }

    public static boolean isValidMacAddress(String mac) {
        return mac == null || mac.isEmpty() || MAC_PATTERN.matcher(mac).matches();
    }

    public static void validateDevice(Device device) {
        if (!isValidIpAddress(device.getIpAddress())) {
            throw new IllegalArgumentException("Invalid IP address format");
        }
        if (!isValidMacAddress(device.getMacAddress())) {
            throw new IllegalArgumentException("Invalid MAC address format");
        }
    }
}