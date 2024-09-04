package com.vaptlab.pratibandhsdk;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

public class ScreenInfo {

    private Context context;
    private long lastExitTime;

    public ScreenInfo(Context context) {
        this.context = context;
        this.lastExitTime = SystemClock.elapsedRealtime();
    }

    // Get Screen Brightness
    public int getScreenBrightness() {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -1; // Error retrieving brightness
        }
    }

    // Get Media Volume
    public int getMediaVolume() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    // Get Sound Effects Volume
    public int getSoundEffectsVolume() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    // Check Flight Mode Status
    public boolean isFlightModeOn() {
        return Settings.System.getInt(
                context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    // Get Last App Exit Time
    public long getLastExitTime() {
        return lastExitTime;
    }

    // Check if App is in Background
    public boolean isAppInBackground() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND &&
                    appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }



}
