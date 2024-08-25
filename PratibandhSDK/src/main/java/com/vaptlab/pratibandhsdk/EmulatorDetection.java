package com.vaptlab.pratibandhsdk;

import android.os.Build;
import java.io.File;

public class EmulatorDetection {

    public static boolean isEmulator() {
        return isGenericBuild() || isKnownModel() || isKnownHardware() || isEmulatedBrand()
                || isEmulatorFromFileSystem() || isEmulatorFromRadio()
                || isEmulatorFromCpu() || isEmulatorFromFingerprint();
    }

    private static boolean isGenericBuild() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.PRODUCT.contains("sdk_gphone")  // Add gphone detection
                || Build.PRODUCT.contains("sdk_google");
    }

    private static boolean isKnownModel() {
        return Build.MODEL.equalsIgnoreCase("google_sdk")
                || Build.MODEL.equalsIgnoreCase("Android SDK built for x86")
                || Build.MODEL.contains("droid4x");
    }

    private static boolean isKnownHardware() {
        return Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.BRAND.equalsIgnoreCase("generic")
                || Build.DEVICE.equalsIgnoreCase("generic")
                || Build.BOARD.equalsIgnoreCase("goldfish")
                || Build.BOARD.equalsIgnoreCase("ranchu")
                || Build.HARDWARE.contains("qemu")  // Add QEMU detection
                || Build.HARDWARE.contains("vbox86"); // Add VirtualBox detection
    }

    private static boolean isEmulatedBrand() {
        return Build.MANUFACTURER.equalsIgnoreCase("unknown")
                || Build.BRAND.equalsIgnoreCase("generic")
                || Build.SERIAL.equalsIgnoreCase("unknown")
                || Build.SERIAL.toLowerCase().contains("emulator")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("generic");
    }

    private static boolean isEmulatorFromFileSystem() {
        return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists()
                || new File("/system/bin/qemu-props").exists();  // Add QEMU props check
    }

    private static boolean isEmulatorFromRadio() {
        String radio = Build.getRadioVersion();
        return radio != null && (radio.contains("generic") || radio.contains("unknown"));
    }

    private static boolean isEmulatorFromCpu() {
        String cpuAbi = Build.SUPPORTED_ABIS[0];
        return cpuAbi.contains("x86") || cpuAbi.contains("armeabi-v7a")
                || Build.SUPPORTED_ABIS.length > 1 && Build.SUPPORTED_ABIS[1].contains("x86");
    }

    private static boolean isEmulatorFromFingerprint() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.FINGERPRINT.contains("vbox")
                || Build.FINGERPRINT.contains("google_sdk")
                || Build.FINGERPRINT.contains("generic_x86")
                || Build.FINGERPRINT.contains("generic_x86_64")
                || Build.FINGERPRINT.contains("vbox86p")
                || Build.FINGERPRINT.contains("generic_x86_64")
                || Build.FINGERPRINT.contains("sdk_google");
    }
}
