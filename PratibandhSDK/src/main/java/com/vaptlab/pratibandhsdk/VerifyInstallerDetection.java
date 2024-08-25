package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

public class VerifyInstallerDetection {

    private static final String PLAY_STORE_PACKAGE_NAME = "com.android.vending";
    private static final String AMAZON_APPSTORE_PACKAGE_NAME = "com.amazon.venezia";
    private static final String TAG = "InstallerDetection";

    public static boolean isInstalledFromValidSource(Context context) {
        try {
            String installerPackageName = context.getPackageManager().getInstallerPackageName(context.getPackageName());

            // If installerPackageName is null or empty, it likely means the app was sideloaded or installed from an unknown source
            if (TextUtils.isEmpty(installerPackageName)) {
                Log.w(TAG, "Installer package name is null or empty. App might have been installed from an unknown source.");
                return false;
            }

            // Check if the app was installed from the Play Store or Amazon Appstore
            boolean isValidSource = PLAY_STORE_PACKAGE_NAME.equals(installerPackageName) || AMAZON_APPSTORE_PACKAGE_NAME.equals(installerPackageName);

            if (!isValidSource) {
                Log.w(TAG, "App was installed from an unknown or untrusted source: " + installerPackageName);
            }

            return isValidSource;

        } catch (Exception e) {
            Log.e(TAG, "Error detecting installer package name", e);
            return false; // Assume invalid source in case of error
        }
    }
}
