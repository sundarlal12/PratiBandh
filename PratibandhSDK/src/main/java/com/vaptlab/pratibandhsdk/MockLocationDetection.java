package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import java.util.List;
public class MockLocationDetection {

//    /**
//     * Checks whether mock location is enabled on the device.
//     *
//     * @param context The context of the calling activity.
//     * @return True if mock location is enabled, false otherwise.
//     */
//    public static boolean isMockLocationEnabled(Context context) {
//        boolean isMockLocation = false;
//
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                // For Android Marshmallow and above, check if the app is in the list of apps allowed to use mock locations
//                isMockLocation = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
//            } else {
//                // For older versions, check if the device is set to allow mock locations
//                isMockLocation = !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return isMockLocation;
//    }

    public static boolean isMockLocationEnabled(Context context) {
        boolean isMockLocation = false;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For Android Marshmallow and above, check if the app is in the list of apps allowed to use mock locations
                isMockLocation = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
            } else {
                // For older versions, check if the device is set to allow mock locations
                isMockLocation = !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
            }

            // Additional check for mock location apps
            if (hasMockLocationApps(context)) {
                isMockLocation = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isMockLocation;
    }

    /**
     * Checks whether there are any mock location apps installed on the device.
     *
     * @param context The context of the calling activity.
     * @return True if any mock location apps are found, false otherwise.
     */
    private static boolean hasMockLocationApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // Get a list of all installed apps
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : apps) {
            String appName = app.packageName;
            // You can add specific checks for known mock location apps here
            // Example (you should replace these with actual mock location app packages):
            if (appName.contains("mocklocation") || appName.contains("fakegps")) {
                return true;
            }
        }

        return false;
    }
}
