package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
//
//public class AppCloneDetection {
//
//    private static final String TAG = "AppCloneDetection";
//
//    public static boolean isAppCloned(Context context, String appName) {
//        if (isPackageNameCloned(context)) {
//            return true;
//        }
//return false;
//     //   return isSignatureCloned(context, appName);
//    }
//
//    public static boolean isPackageNameCloned(Context context) {
//        ApplicationInfo applicationInfo = context.getApplicationInfo();
//        String packageName = applicationInfo.packageName;
//
//        if (packageName != null && packageName.contains("clone")) {
//            Log.d(TAG, "Package name indicates a cloned app!");
//            return true;
//        }
//
//        return false;
//    }
//
////    public static boolean isSignatureCloned(Context context, String appName) {
////        String packageName = context.getPackageName();
////
////        String expectedSignatureHash = SignatureUtils.getExpectedSignatureHash(context, packageName, appName);
////
////        String currentSignatureHash = SignatureUtils.generateCurrentSignatureHash(context);
////
////        if (expectedSignatureHash != null && !expectedSignatureHash.equals(currentSignatureHash)) {
////            Log.d(TAG, "App signature mismatch detected! Possible clone.");
////            return true;
////        }
////
////        return false;
////    }
//
//
//
//
//    public static void isSignatureCloned(Context context, String appName) {
//        String packageName = context.getPackageName();
//
//        SignatureUtils.getExpectedSignatureHash(context, packageName, appName, new SignatureUtils.SignatureHashCallback() {
//            @Override
//            public void onSuccess(String expectedSignatureHash) {
//                String currentSignatureHash = SignatureUtils.generateCurrentSignatureHash(context);
//
//                if (expectedSignatureHash != null && !expectedSignatureHash.equals(currentSignatureHash)) {
//                    Log.d(TAG, "App signature mismatch detected! Possible clone.");
//                }
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Log.e(TAG, "Failed to retrieve expected signature hash: " + error);
//            }
//        });
//    }
//}


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Locale;

public class AppCloneDetection {

    private static final String TAG = "AppCloneDetection";

    public static boolean isAppCloned(Context context, String appName) {
        if (isPackageNameCloned(context) || isRunningInParallelSpace(context) || hasDualAppProperty() || isInstalledInUnusualLocation(context)) {
            return true;
        }
        return false;
    }

    /**
     * Check if the package name suggests the app is cloned.
     */
    public static boolean isPackageNameCloned(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String packageName = applicationInfo.packageName;

        if (packageName != null && packageName.toLowerCase(Locale.ROOT).contains("clone")) {
            Log.d(TAG, "Package name indicates a cloned app!");
            return true;
        }
        return false;
    }

    /**
     * Check if the app is running in a known parallel space environment.
     */
    public static boolean isRunningInParallelSpace(Context context) {
        String[] knownCloneApps = {
                "com.parallel.space",
                "com.dual.parallel.multiaccounts",
                "com.lbe.parallel.intl",
                "com.cloneapp.parallelspace.dualspace"
        };

        for (String cloneApp : knownCloneApps) {
            if (isAppInstalled(context, cloneApp)) {
                Log.d(TAG, "Detected app running in a parallel space environment!");
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the app is installed in an unusual location, which might indicate a cloned app.
     */
//    public static boolean isInstalledInUnusualLocation() {
//        String appPath = Environment.getDataDirectory().getAbsolutePath();
//        if (!appPath.startsWith("/data/")) {
//            Log.d(TAG, "App installed in an unusual location, possible cloning detected!");
//            return true;
//        }
//        return false;
//    }
    public static boolean isInstalledInUnusualLocation(Context context) {
        String appPath = context.getApplicationInfo().sourceDir;

        // The usual app installation path on most Android devices
        String expectedPathPrefix = "/data/app/";

        // If the app is not installed in the typical /data/app/ directory
        if (!appPath.startsWith(expectedPathPrefix)) {
            Log.d(TAG, "App installed in an unusual location: " + appPath + ", possible cloning detected!");
            return true;
        }

        return false;
    }


    /**
     * Check if certain system properties indicate a dual app or cloned environment.
     */
    public static boolean hasDualAppProperty() {
        String[] dualAppProperties = {
                "ro.dualapp.enabled",
                "ro.parallelspace.enabled",
                "ro.multiapp.support"
        };

        for (String property : dualAppProperties) {
            String value = getSystemProperty(property);
            if (value != null && value.equals("true")) {
                Log.d(TAG, "System property indicates a dual app or cloned environment!");
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to check if a specific app is installed.
     */
    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Helper method to get a system property.
     */
    private static String getSystemProperty(String key) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("get", String.class);
            return (String) method.invoke(null, key);
        } catch (Exception e) {
            return null;
        }
    }
}
