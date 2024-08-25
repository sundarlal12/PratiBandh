package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.provider.Settings;

public class UsbDebuggingDetection {

    public static boolean isUsbDebuggingEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) == 1;
    }
}
