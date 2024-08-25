package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class ChecksumUtility {

    private static final String TAG = "ChecksumUtility";

    /**
     * Computes the SHA-256 checksum for a given file.
     *
     * @param file The file to compute the checksum for.
     * @return The computed checksum as a string.
     */
    public static String computeChecksum(File file) {
        try (InputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }

            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Computes the checksum of the APK of the given context.
     *
     * @param context The context from which to get the APK.
     * @return The computed checksum as a string.
     */
    public static String computeApkChecksum(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            File apkFile = new File(packageInfo.applicationInfo.sourceDir);
            return computeChecksum(apkFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Validates the APK checksum against an expected checksum.
     *
     * @param context The context from which to get the APK.
     * @param expectedChecksum The expected checksum value.
     * @return True if the checksums match, false otherwise.
     */
    public static boolean isApkChecksumValid(Context context, String expectedChecksum) {
        String computedChecksum = computeApkChecksum(context);
        return expectedChecksum != null && expectedChecksum.equals(computedChecksum);
    }

    /**
     * Fetches the original checksum from the server and handles the logic for calculating
     * and storing the checksum if the server returns an empty response.
     *
     * @param context The context from which to get the APK.
     * @return The checksum fetched from the server or computed locally.
     */
    public static String checksumGetter(Context context) {
        String originalChecksum = fetchChecksumFromServer(context);
        if (originalChecksum == null || originalChecksum.isEmpty()) {
            // Server returned an empty checksum, calculate it locally and send to the server
            String computedChecksum = computeApkChecksum(context);
            if (computedChecksum != null) {
                NetworkHelper.sendChecksumToServer(context, computedChecksum);
                return computedChecksum;
            } else {
                Log.e(TAG, "Failed to compute APK checksum.");
                return null;
            }
        } else {
            // Server returned a valid checksum
            return originalChecksum;
        }
    }

    /**
     * Fetches the checksum from the server using NetworkHelper.
     *
     * @param context The context from which to make the network request.
     * @return The checksum as a string or null if an error occurred.
     */
    private static String fetchChecksumFromServer(Context context) {
        String url = "https://sundar.com/checsumgetter";
        return NetworkHelper.sendGetRequest(context, url);
    }
}
