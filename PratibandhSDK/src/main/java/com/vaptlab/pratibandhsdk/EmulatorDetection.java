package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.os.Build;
import java.io.File;

public class EmulatorDetection {

    private Context context;

    public EmulatorDetection(Context context) {
        this.context = context;
    }

    public boolean isEmulator() {
        return isGenericBuild() || isKnownModel() || isKnownHardware()
                || isEmulatorFromFileSystem() || isEmulatorFromRadio()
                 || isEmulatorFromFingerprint() || isEmulatorFromApps() || isEmulatorFromCpu();// ||isEmulatedBrand();
    }

    public boolean isGenericBuild() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.PRODUCT.contains("sdk_gphone")
                || Build.PRODUCT.contains("sdk_google");
    }

    public boolean isKnownModel() {
        return Build.MODEL.equalsIgnoreCase("google_sdk")
                || Build.MODEL.equalsIgnoreCase("Android SDK built for x86")
                || Build.MODEL.contains("droid4x");
    }

    public boolean isKnownHardware() {
        return Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.BRAND.equalsIgnoreCase("generic")
                || Build.DEVICE.equalsIgnoreCase("generic")
                || Build.BOARD.equalsIgnoreCase("goldfish")
                || Build.BOARD.equalsIgnoreCase("ranchu")
                || Build.HARDWARE.contains("qemu")
                || Build.HARDWARE.contains("vbox86");
    }

//    public boolean isEmulatedBrand() {
//        return Build.MANUFACTURER.equalsIgnoreCase("unknown")
//                || Build.BRAND.equalsIgnoreCase("generic")
//                || Build.SERIAL.equalsIgnoreCase("unknown")
//                || Build.PRODUCT.contains("google_sdk")
//                || Build.PRODUCT.contains("sdk")
//                || Build.DEVICE.contains("generic")
//                || Build.DEVICE.contains("unknown")
//                || Build.MODEL.contains("sdk")
//                || Build.MODEL.contains("emulator");
//    }

    public boolean isEmulatorFromFileSystem() {
        return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists()
                || new File("/system/bin/qemu-props").exists();
    }

    public boolean isEmulatorFromRadio() {
        String radio = Build.getRadioVersion();
        return radio != null && (radio.contains("generic") || radio.contains("unknown") || radio.contains("emulator"));
    }

    public boolean isEmulatorFromCpu() {
        String[] cpuAbis = Build.SUPPORTED_ABIS;
        boolean hasEmulatorAbi = false;

        // Look for known emulator ABIs
        for (String abi : cpuAbis) {
            if (abi.contains("x86") || abi.contains("x86_64") || abi.contains("armeabi-v7a") || abi.contains("arm64-v8a")) {
                hasEmulatorAbi = true;
            }
        }

        // Further checks for emulators (e.g., ABI + other known emulated device attributes)
        if (hasEmulatorAbi) {
            // Perform additional checks if necessary, e.g., Build.PRODUCT, Build.BRAND
            return Build.BRAND.startsWith("generic") || Build.DEVICE.startsWith("generic") || Build.PRODUCT.contains("sdk");
        }

        return false;
    }

    public boolean isEmulatorFromFingerprint() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.FINGERPRINT.contains("vbox")
                || Build.FINGERPRINT.contains("google_sdk")
                || Build.FINGERPRINT.contains("generic_x86")
                || Build.FINGERPRINT.contains("generic_x86_64")
                || Build.FINGERPRINT.contains("sdk_google")
                || Build.FINGERPRINT.contains("vbox86p")
                || Build.FINGERPRINT.contains("generic_x86_64");
    }

    public boolean isEmulatorFromApps() {
        // Check for presence of emulator apps or services
        String[] knownEmulators = {"com.android.emulator", "com.google.android.emulator"};
        for (String app : knownEmulators) {
            try {
                context.getPackageManager().getPackageInfo(app, 0);
                return true;
            } catch (Exception e) {
                // Package not found, continue
            }
        }
        return false;
    }
}
