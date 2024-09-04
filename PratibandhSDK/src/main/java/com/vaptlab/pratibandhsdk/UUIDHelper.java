package com.vaptlab.pratibandhsdk;import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

public class UUIDHelper {

    private static final String PREFS_NAME = "DevicePrefs";
    private static final String UUID_KEY = "device_uuid";
    private static final String SCREENSHOT_PREVENTION_KEY = "screenshot_prevention_enabled";

    private Context context;

    public UUIDHelper(Context context) {
        this.context = context;
    }

    public String getDeviceUUID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString(UUID_KEY, null);

        if (uuid == null) {
            uuid = generateDeviceUUID();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(UUID_KEY, uuid);
            editor.apply();
        }

        return uuid;
    }

    public String generateDeviceUUID() {
        String deviceId = getDeviceId();
        String deviceInfo = Build.BRAND + Build.MODEL + Build.DEVICE + Build.VERSION.RELEASE + deviceId;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(deviceInfo.getBytes());

            // Convert hash to base 36 (0-9, a-z)
            String base36Hash = new java.math.BigInteger(1, hash).toString(36);

            // Ensure the length is exactly 29 characters
            if (base36Hash.length() > 29) {
                return base36Hash.substring(0, 29);
            } else {
                // Pad with zeros if less than 29 characters
                return String.format("%1$-" + 29 + "s", base36Hash).replace(' ', '0');
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return UUID.randomUUID().toString().substring(0, 29); // Fallback with a trimmed UUID
        }
    }

    private String getDeviceId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android 8.0 and above
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            // For older versions
            return Build.SERIAL;
        }
    }
    private String bytesToHex(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    public void setScreenshotPreventionEnabled(boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SCREENSHOT_PREVENTION_KEY, isEnabled);
        editor.apply();
    }


    public boolean isScreenshotPreventionEnabled() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SCREENSHOT_PREVENTION_KEY, false);
    }
}
