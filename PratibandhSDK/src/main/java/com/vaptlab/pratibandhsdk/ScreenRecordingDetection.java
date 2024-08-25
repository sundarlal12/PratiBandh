package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

public class ScreenRecordingDetection {

    private static final String TAG = "ScreenRecordingDetector";

    private Context context;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private boolean isRecordingActive = false; // Flag to track recording state
    private ScreenRecordingCallback callback;
    private static final int SCREEN_RECORD_REQUEST_CODE = 1000;

    public ScreenRecordingDetection(Context context) {
        this.context = context;
        this.mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    /**
     * Start screen recording with user consent.
     *
     * @param activity The activity from which the recording is requested.
     * @param callback Called when the recording starts successfully.
     */
//    public void startScreenRecording(Activity activity, ScreenRecordingCallback callback) {
//        this.callback = callback;
//        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
//        activity.startActivityForResult(intent, SCREEN_RECORD_REQUEST_CODE);
//    }

    /**
     * Handle the result of screen recording permission request.
     *
     * @param requestCode The request code passed in startActivityForResult().
     * @param resultCode The result code returned by the screen recording activity.
     * @param data The intent data returned by the screen recording activity.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                isRecordingActive = true; // Update the flag
                if (callback != null) {
                    callback.onRequestPermissionsResult(true);
                }
            } else {
                if (callback != null) {
                    callback.onRequestPermissionsResult(false);
                }
            }
        }
    }

    /**
     * Check if screen recording is currently in progress.
     *
     * @return True if screen recording is active, false otherwise.
     */
    public boolean isScreenRecordingActive() {
        return isRecordingActive;
    }

    /**
     * Stop screen recording.
     */
    public void stopScreenRecording() {
        if (mediaProjection != null && isRecordingActive) {
            mediaProjection.stop();
            mediaProjection = null;
            isRecordingActive = false; // Update the flag
        }
    }

    /**
     * Callback interface for screen recording events.
     */
    public interface ScreenRecordingCallback {
        void onRequestPermissionsResult(boolean granted);
        void onFailure(String errorMessage);
    }
}
