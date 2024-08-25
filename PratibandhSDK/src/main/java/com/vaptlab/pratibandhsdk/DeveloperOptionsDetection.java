package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.provider.Settings;

public class DeveloperOptionsDetection {

    public static boolean isDeveloperOptionsEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
    }
}
