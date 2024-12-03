package com.vaptlab.pratibandhsdk;
import android.annotation.SuppressLint;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.os.Looper;
import android.widget.Toast;
import android.util.Log;
import java.util.Map;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Debug;
import android.os.FileObserver;
import android.provider.Settings;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.security.MessageDigest;
import java.util.Arrays;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//private ExecutorService executorService;
public class SecuritySDK {
String exp="1";
    private static final String TAG = "SecuritySDK";
    String curt="2";
    private static Activity activity;
    private static GLSurfaceView mGLSurfaceView;  // Reference to GLSurfaceView
    private static CustomAlertDialogFragment dialogFragment;

    private Context context;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static SecuritySDK instance;
   // private Context context;
    private String serverUrl;
    private String runtimeclone;
    private boolean isScreenshotDetected = false;

    private String runtimeServerUrl;
    private String licenseKey;


    private UUIDHelper uuidHelper;
   private FileObserver screenshotObserver;

    private ScreenshotDetection screenshotDetection;

    //  private static final String EXPECTED_SIGNATURE = "your-expected-signature"; // Replace with actual expected signature
    private static final String EXPECTED_CLASS_CHECKSUM = "your-expected-checksum"; // Replace with actual expected checksum

   private ExecutorService executorService;


    private SecuritySDK(Context context,String licenseKey) {

    //    this.context = context.getApplicationContext();
        this.context = context;
        this.serverUrl = "https://report.vaptlabs.com/pratibandhAPI/metrics.php";
        this.runtimeServerUrl = "https://report.vaptlabs.com/pratibandhAPI/run_time_decteced_metrics.php";

        this.runtimeclone="https://report.vaptlabs.com/pratibandhAPI/Set_orginial_signature.php";

        this.executorService = Executors.newFixedThreadPool(4);
        this.screenshotDetection = new ScreenshotDetection(context);
        this.uuidHelper = new UUIDHelper(context);
        this.licenseKey = licenseKey;



    }

    public static void initialize(Activity activity) {
        SecuritySDK.activity = activity;
        // Initialize OpenGL-related resources here if needed
    }

    public static SecuritySDK getInstance(Context context, String licenseKey) {
        if (instance == null) {
            instance = new SecuritySDK(context, licenseKey);
        }
        return instance;
    }


public static void initializeAndCheckSecurity(Activity activity, String licenseKey) {
    SecuritySDK securitySDK = new SecuritySDK(activity, licenseKey);
    securitySDK.performSecurityCheck(activity);
}


    private void performSecurityCheck(Activity activity) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 100);
        } else {
            // Proceed with security check if permission is granted
            proceedWithSecurityCheck(activity);
        }
    }


    public void showAlert(Context context, String title,String message) {
        // Use DialogHelper to show the alert dialog
        DialogHelper.showCustomAlertDialog(context, message,title);
    }

    public void checkActivityLifecycle(Activity activity) {
        // Log the activity lifecycle event (you can modify this based on what you need to do)
        if (activity != null) {
            Log.d(TAG, "Activity lifecycle checked for: " + activity.getClass().getSimpleName());
        }
    }

    private void proceedWithSecurityCheck(Activity activity) {
        JSONObject securityMetrics = new JSONObject();
        JSONObject securityMetrics1 = new JSONObject();
        USBdetection usbDetection = new USBdetection(context);

        boolean isUsbConnected = usbDetection.isUsbConnected();
        boolean securityIssueDetected = false;
        boolean securityIssueDetected1 = false;
        int issues=0;

        try {
            // Get package name, app name, and device information
            String packageName = context.getPackageName();
            String appName = getAppName(context);
            String deviceId = getDeviceId(context);
            String deviceName = getDeviceName();
            String imei =getDeviceId(context); //getIMEINumber(context);
            String deviceType = getDeviceType();
         //   String carrier = DeviceInfo.getCarrierInfo();
            String sig_hash=SignatureUtils.generateCurrentSignatureHash(context);

            //retrieveExpectedSignatureHash(context);
            // String deviceInfo = getDeviceInfo();
            JSONObject deviceInfo = getDeviceInfo();
            // Add package name, app name, and device information to metrics
           // String mac = MacAddressDetection.getMacAddress(context);

            securityMetrics.put("LicenceKey",licenseKey);

            if( uuidHelper.isScreenshotPreventionEnabled()) {
                securityMetrics.put("ScreenDetectProtection", enableProtection());
            }
            else{securityMetrics.put("ScreenDetectProtection", false);
            }
          //  securityMetrics.put("mac-address", mac);
            securityMetrics.put("Signature_Hash", sig_hash);

          //  deviceInfo.put("uuid", uuidHelper.getDeviceUUID());
            if(isUsbConnected) {
                securityMetrics.put("is_usb_connected", isUsbConnected);
                securityIssueDetected = true;

                showAlert(context,"It appears that the device is connected via USB. Kindly disconnect the USB and disable the developer options to continue using the app","USB debugging Enabled!!");


                issues++;
            }


            securityMetrics.put("UUID", uuidHelper.getDeviceUUID());
            securityMetrics.put("package_name", packageName);
            securityMetrics.put("app_name", appName);
            securityMetrics.put("device_id", deviceId);
            securityMetrics.put("device_name", deviceName);
            // securityMetrics.put("IMEI_no", imei);
            securityMetrics.put("device_type", deviceType);
            securityMetrics.put("device_information", deviceInfo.toString());

            int apiLevel = Build.VERSION.SDK_INT;
            String androidVersion = Build.VERSION.RELEASE;
            securityMetrics.put("android_api_level", apiLevel);
            securityMetrics.put("android_version", androidVersion);


            if (imei != null) {
                securityMetrics.put("IMEI_no", imei);
            } else {
                securityMetrics.put("IMEI_no", "Permission not granted or not available"); // Custom message
            }




            //code

            Map<String, String> systemProxyDetails = ProxyDetection.getSystemProxyDetails();
            if (!systemProxyDetails.isEmpty()) {
                // Combine HTTP proxy details
                String httpSystemProxyIp = systemProxyDetails.get("system_http_proxy_ip");
                String httpSystemProxyPort = systemProxyDetails.get("system_http_proxy_port");
                if (httpSystemProxyIp != null && httpSystemProxyPort != null) {
                    String combinedSystemHttpProxy = httpSystemProxyIp + ":" + httpSystemProxyPort;
                    securityMetrics.put("SystemHTTPProxy", combinedSystemHttpProxy);
                }

                // Combine HTTPS proxy details
                String httpsSystemProxyIp = systemProxyDetails.get("system_https_proxy_ip");
                String httpsSystemProxyPort = systemProxyDetails.get("system_https_proxy_port");
                if (httpsSystemProxyIp != null && httpsSystemProxyPort != null) {
                    String combinedSystemHttpsProxy = httpsSystemProxyIp + ":" + httpsSystemProxyPort;
                    securityMetrics.put("SystemProxy", combinedSystemHttpsProxy);
                }

                Log.d("ProxyDetection", "System Proxy detected: " + systemProxyDetails);
                showAlert(context,"This device appears to be connected to an unsecured network. For optimal security, it is recommended to connect to a secure network before continuing to use the app.","Proxy Server Detected!");

                securityIssueDetected = true;
              issues++;

            }

            // Retrieve network proxy details

            Map<String, String> networkProxyDetails = ProxyDetection.getNetworkProxyDetails(context);
            if (!networkProxyDetails.isEmpty()) {
                // Combine network proxy details
                String networkProxyIp = networkProxyDetails.get("network_proxy_ip");
                String networkProxyPort = networkProxyDetails.get("network_proxy_port");
                if (networkProxyIp != null && networkProxyPort != null) {
                    String combinedNetworkProxy = networkProxyIp + ":" + networkProxyPort;
                    securityMetrics.put("NetworkProxy", combinedNetworkProxy);
                }

                Log.d("ProxyDetection", "Network Proxy detected: " + networkProxyDetails);
                showAlert(context,"This device appears to be connected to an unsecured network. For optimal security, it is recommended to connect to a secure network before continuing to use the app.","Proxy Server Detected!");

                securityIssueDetected = true;
                issues++;
            }

            //code


            if (RootDetection.isDeviceRooted()) {
                Log.d("issue#1", "Rooted device");
             //   showToast("Root detection detected!");

                securityMetrics.put("isRooted", true);
                securityIssueDetected = true;

                showAlert(context,"This device has been identified as rooted. For optimal functionality and security, it is recommended to use this application on a non-rooted device.","Rooting Detected!");

                issues++;
            } else {
                securityMetrics.put("isRooted", false);
            }
//            showToast("Emulator :"+EmulatorDetection.isEmulator());
            EmulatorDetection emulatorDetection = new EmulatorDetection(context);

            if (emulatorDetection.isEmulator()) {
                Log.d("issue#2", "Emulator detected");
       //         showToast("Emulator detected!");
                securityMetrics.put("isEmulator", true);
                securityIssueDetected = true;
                showAlert(context,"It appears that the app is running in an emulator environment. For an optimal user experience, we recommend using the app on a real device.","Emulator Detected!");


                issues++;
            } else {
                securityMetrics.put("isEmulator", false);
            }

            if (UsbDebuggingDetection.isUsbDebuggingEnabled(context)) {
                Log.d("issue#3", "USB debugging enabled");
  //              showToast("USB debugging detected!");
                securityMetrics.put("isUsbDebuggingEnabled", true);
                securityIssueDetected = true;

                showAlert(context,"It appears that the device is connected via USB. Kindly disconnect the USB and disable the developer options to continue using the app","USB debugging Enabled!!");

                issues++;
            } else {
                securityMetrics.put("isUsbDebuggingEnabled", false);
            }

            if (DeveloperOptionsDetection.isDeveloperOptionsEnabled(context)) {
                Log.d("issue#4", "Developer options enabled");
     //           showToast("Developer options enabled!");
                securityMetrics.put("isDeveloperOptionsEnabled", true);
                securityIssueDetected = true;
                showAlert(context,"It appears that the developer options are enabled on the device. Kindly disable the developer options to continue using the app. Thank you for your cooperation.","Developer Option Enabled!!");

                issues++;
            } else {
                securityMetrics.put("isDeveloperOptionsEnabled", false);
            }

            if (!VerifyInstallerDetection.isInstalledFromValidSource(context)) {
                Log.d("issue#5", "App not installed from a valid source");
             //   showToast("App not installed from a valid source!");
                securityMetrics.put("isInstalledFromValidSource", false);
               // securityIssueDetected = true;
                showAlert(context,"It appears that the app has not been installed from a valid source, such as the Play Store or Amazon Store. For security and optimal performance, please ensure the app is installed from a trusted source. Thank you.","Invalid trusted source!!");

                issues++;
            } else {
                securityMetrics.put("isInstalledFromValidSource", true);
            }

            if (AppCloneDetection.isAppCloned(context,appName)) {
                Log.d("issue#6", "App clone detected");
//                showToast("App clone detected!");
                securityMetrics.put("isAppCloned", true);
                securityMetrics.put("extra", AppCloneDetection.isInstalledInUnusualLocation(context));
                //securityMetrics1.put("isAppCloned", AppCloneDetection.isInstalledInUnusualLocation());
                showAlert(context,"It appears that the app is either cloned or running in an untrusted and unsecured environment. For security and optimal performance, it is recommended to use the app in a trusted and secure environment.","App Clone Detected!!");

                securityIssueDetected = true;
                securityIssueDetected1 = true;
                issues++;
                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);

            } else {
                securityMetrics.put("isAppCloned", false);
            }

            //code


            //code

            if (Debug.isDebuggerConnected()) {
                Log.d("issue#8", "Debugging detected");
//                showToast("Debugging detected!");
                securityMetrics.put("isDebugging", true);
                securityIssueDetected = true;
                showAlert(context,"It appears that the device is connected via USB. Kindly disconnect the USB and disable the developer options to continue using the app","USB debugging Enabled!!");

                issues++;
            } else {
                securityMetrics.put("isDebugging", false);
            }

//            if (hasAppBeenTamperedWith()) {
//                Log.d("issue#9", "App tampering detected");
////                showToast("App tampering detected!");
//                securityMetrics.put("isTampered", true);
//                securityIssueDetected = true;
                  issues++;
//            } else {
//                securityMetrics.put("isTampered", false);
//            }

            if (isVPNEnabled()) {
                Log.d("issue#10", "VPN detected");
//                showToast("VPN detected!");
                securityMetrics.put("isVPNEnabled", true);

                showAlert(context,"It appears that a VPN is enabled on your device. For optimal performance and security, please disable the VPN and try again. Thank you for your cooperation.","VPN Detected!!");


                securityIssueDetected = true;
                issues++;
            } else {
                securityMetrics.put("isVPNEnabled", false);
            }
//            if (isProxyEnabled()) {
//                Log.d("issue#10", "Proxy enabled");
//                showToast("Proxy detected!");
//                securityMetrics.put("isProxyEnabled", true);
//                securityIssueDetected = true;
                  issues++;
//            } else {
//                securityMetrics.put("isProxyEnabled", false);
//            }

            if (ProxyDetection.isProxyEnabled(context)) {
                Log.d("issue#10", "Proxy enabled");
//                showToast("Proxy detected!");
                securityMetrics.put("isProxyEnabled", true);
                securityIssueDetected = true;

                showAlert(context,"This device appears to be connected to an unsecured network. For optimal security, it is recommended to connect to a secure network before continuing to use the app.","Proxy Server Detected!");


                issues++;
            } else {
                securityMetrics.put("isProxyEnabled", false);
            }

            if (isScreenRecording(context)) {
                Log.d("issue#11", "Screen recording detected");
//                showToast("Screen recording detected!");
                securityMetrics.put("isScreenRecording", true);

                showAlert(context,"Screen recording has been detected on your device. For security and privacy reasons, please disable screen recording to continue using the app. Thank you for your understanding.","Screen Recording Detected!");

                securityIssueDetected = true;
                securityIssueDetected1 = true;
           //     issues++;

                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);
            } else {
                securityMetrics.put("isScreenRecording", false);
            }


//            if (isScreenshotDetected()) {
//                Log.d("issue#12", "Screenshot detected");
//                showToast("Screenshot detected!");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
                  issues++;
//            } else {
//                securityMetrics.put("isScreenshotDetected", false);
//            }
//
//            if (screenshotDetection != null && screenshotDetection.isScreenshotDetected()) {
//                Log.d("SecuritySDK", "Screenshot detected");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
                  issues++;
//                showToast("screenshot detected!");
//              //  sendMetricsToServer(securityMetrics);
//            }


//            if (screenshotDetection.isScreenshotDetected()) {
//                Log.d("SecuritySDK", "Screenshot detected");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
                  issues++;
//                sendRuntimeMetricsToServer(securityMetrics);
//
//                //  sendMetricsToServer(securityMetrics);
//            }
//            if (screenshotDetection.isScreenshotDetected()) {
//                Log.d("SecuritySDK", "Screenshot detected");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
//                securityIssueDetected1 = true;
                  issues++;
//                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);
//            }


            if (MockLocationDetection.isMockLocationEnabled(context)) {
                Log.d("issue#13", "Mock location detected");
//                showToast("Mock location detected!");
                securityMetrics.put("isMockLocationEnabled", true);
                securityIssueDetected = true;

                showAlert(context,"It appears that mock location or spoof location is enabled on your device. For accurate functionality, please disable these settings and use the app in a genuine location environment.","Mock location Detected!");

                issues++;
            } else {
                securityMetrics.put("isMockLocationEnabled", false);
            }


            //keep it as last deetction

            isAppSignatureValid(context, new SignatureValidationCallback() {
                @Override
                public void onValidationResult(boolean isValid) {
                    try {
                        if (isValid) {
                            securityMetrics.put("isSignatureValid", true);
                            Log.d("test", "App signature is valid.");
                        } else {
                            securityMetrics.put("isSignatureValid", false);
                            Log.d("test", "App signature is not valid.");
                            showAlert(context,"The app signature is invalid, or the app may have been tampered with. For security reasons, please ensure the app is authentic and has not been modified.","App Tempering Detected!");

                            //    issues++;
                        }
                        // Send metrics to server after setting the value
                          sendMetricsToServer(securityMetrics);
                      //  issues++;

                    } catch (Exception e) {
                        Log.e("tes", "Error creating JSON object", e);
                    }
                }
            });


            //last detction


            if (securityIssueDetected) {
                sendMetricsToServer(securityMetrics);
        //     showToast("We got "+issues+" issues in your device,App will close automatically");

           // showCustomAlertDialog(context,"Root Detected");
//                issues=0;
//                new Handler().postDelayed(() -> exitApp(activity), 5000); // Delay of 20 seconds
            }
            else{
                sendMetricsToServer(securityMetrics);
            }

            if (securityIssueDetected1) {
                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);
                sendMetricsToServerruntime(securityMetrics);
            //    new Handler().postDelayed(() -> exitApp(activity), 5000); // Delay of 20 seconds
            }

//            else{
//                sendMetricsToServerruntime(securityMetrics);
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isVPNEnabled() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_VPN;
    }

    private boolean isScreenRecording(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            return mediaProjectionManager != null && isMediaProjectionActive(context);
        }
        return false;
    }

    private boolean isMediaProjectionActive(Context context) {
        return false; // There is no direct method to detect active media projection, so returning false.
    }



    private boolean hasAppBeenTamperedWith() {
        String classChecksum = computeChecksum(this.getClass());
        return !EXPECTED_CLASS_CHECKSUM.equals(classChecksum);
    }

    private String computeChecksum(Class<?> clazz) {
        try {
            byte[] classBytes = clazz.getName().getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(classBytes);
            return Arrays.toString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



    public interface SignatureValidationCallback {
        void onValidationResult(boolean isValid);
    }

    public void isAppSignatureValid(Context context, SignatureValidationCallback callback) {
        // Retrieve the expected signature hash from the server
        if (context == null) {
            Log.e("SecuritySDK", "Context is null, cannot proceed.");
            callback.onValidationResult(false);
            return;
        }
        SignatureUtils.getExpectedSignatureHash(context, context.getPackageName(), SignatureUtils.getAppName(context), new SignatureUtils.SignatureHashCallback() {
            @Override
            public void onSuccess(String expectedSignatureHash,String isScreenshotAllowed) {
                // Generate the current signature hash
                String currentSignatureHash = SignatureUtils.generateCurrentSignatureHash(context);

                Log.d("current_signture", "current: " + currentSignatureHash);
                Log.d("Original_Signature", "original: " + expectedSignatureHash);

                // Compare the hashes
                boolean isValid = expectedSignatureHash != null && expectedSignatureHash.equals(currentSignatureHash);
                Log.d("tes", "signature check: " + isValid);
         if(isScreenshotAllowed.equals("1")){
          uuidHelper.setScreenshotPreventionEnabled(true);
          }
         else{
             uuidHelper.setScreenshotPreventionEnabled(false);
         }

                // Notify the caller via callback
                callback.onValidationResult(isValid);
            }

            @Override
            public void onFailure(String error) {
                // Handle the error (e.g., log it or show a toast)
                Log.e("SecuritySDK", "Failed to get expected signature hash: " + error);

                // Notify the caller of failure
                callback.onValidationResult(false);
            }
        });
    }





    private String getAppName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private String getDeviceName() {
        return Build.MODEL;
    }

    @SuppressLint("MissingPermission")
    public static String getHardwareSerialNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }

    public boolean enableProtection() {
        if (screenshotDetection != null) {
         //   screenshotDetection.cleanup();
            cleanup();
            return screenshotDetection.applyProtection();


        }
        return false;
    }

    public void cleanup() {
        if (screenshotDetection != null) {
            screenshotDetection.cleanup();
        }
    }


//    private String getIMEINumber(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return "Permission not granted";
//        }
//        return telephonyManager.getDeviceId();  // This method is deprecated in Android Q (API 29+)
//    }




    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Activity activity) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedWithSecurityCheck(activity);
            }
//            else {
//                showToast("Permission denied. Some security features may not work.");
//            }
        }
    }

    private String getDeviceType() {
        return Build.DEVICE;
    }

//    private String getDeviceInfo() {
//        return "Brand: " + Build.BRAND + ", Model: " + Build.MODEL + ", Manufacturer: " + Build.MANUFACTURER + ", Android Version: " + Build.VERSION.RELEASE;
//    }

    private JSONObject getDeviceInfo() {
        JSONObject deviceInfo = new JSONObject();
        DeviceInfo deviceInfo1 = new DeviceInfo(context);
        ScreenInfo screenInfo = new ScreenInfo(context);

        try {
            deviceInfo.put("brand", Build.BRAND);
            deviceInfo.put("model", Build.MODEL);
            deviceInfo.put("manufacturer", Build.MANUFACTURER);
            deviceInfo.put("android_version", Build.VERSION.RELEASE);
            deviceInfo.put("IP-Address",  deviceInfo1.getIpAddress());
          //  deviceInfo.put("mac-address",  deviceInfo1.getMacAddress());

            JSONArray abisArray = new JSONArray();
            for (String abi : Build.SUPPORTED_ABIS) {
                abisArray.put(abi);
            }
            deviceInfo.put("ABIS", abisArray);


            JSONArray fingerprintsArray = new JSONArray();
            fingerprintsArray.put(Build.FINGERPRINT);
            fingerprintsArray.put(Build.BOARD); // Example additional property
            fingerprintsArray.put(Build.DEVICE); // Example additional property
            deviceInfo.put("fingerprints", fingerprintsArray);

            // Create a JSONArray for known properties that are potential arrays
            JSONArray modelArray = new JSONArray();
            modelArray.put(Build.MODEL);
            modelArray.put("google_sdk"); // Example of additional value
            deviceInfo.put("models", modelArray);

            JSONArray hardwareArray = new JSONArray();
            hardwareArray.put(Build.HARDWARE);
            hardwareArray.put("goldfish"); // Example of additional value
            hardwareArray.put("ranchu");   // Example of additional value
            deviceInfo.put("hardwares", hardwareArray);

            JSONArray productArray = new JSONArray();
            productArray.put(Build.PRODUCT);
            productArray.put("sdk_google"); // Example of additional value
            deviceInfo.put("products", productArray);

            JSONArray brandArray = new JSONArray();
            brandArray.put(Build.BRAND);
            brandArray.put("generic"); // Example of additional value
            deviceInfo.put("brands", brandArray);

//           device info

            deviceInfo.put("mobiledata", new JSONObject(deviceInfo1.getCarrierInfo()));
            deviceInfo.put("wifi", new JSONObject(deviceInfo1.getWifiInfo()));
            deviceInfo.put("battery", new JSONObject(deviceInfo1.getBatteryInfo()));

//screeninfo

            deviceInfo.put("screenBrightness", screenInfo.getScreenBrightness()); // Use the instance to call the method

            deviceInfo.put("mediavolume", screenInfo.getMediaVolume()); // Use the instance to call the method
            deviceInfo.put("soundeffectvolume", screenInfo.getSoundEffectsVolume()); // Use the instance to call the method
            deviceInfo.put("flightmode", screenInfo.isFlightModeOn()); // Use the instance to call the method
        //    deviceInfo.put("lastexitTime", screenInfo.getLastExitTime()); // we will fix it later

            deviceInfo.put("appbackground", screenInfo.isAppInBackground()); // Use the instance to call the method



//            deviceInfo.put("wifi", new JSONObject(deviceInfo1.getWifiInfo()));
//            deviceInfo.put("battery", new JSONObject(deviceInfo1.getBatteryInfo()));




        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }




    public void sendMetricsToServer(JSONObject metrics) {
        NetworkHelper.sendPostRequest(context, serverUrl, metrics.toString());
    }
    public void sendMetricsToServerruntime(JSONObject metrics) {
        NetworkHelper.sendPostRequest(context, runtimeServerUrl, metrics.toString());
    }

    private void sendRuntimeMetricsAndCloseApp(JSONObject metrics, Activity activity) {
        sendRuntimeMetricsToServer(metrics);
    //    new Handler().postDelayed(() -> exitApp(activity), 5000); // Close app after 20 seconds
    }


    public void sendRuntimeMetricsToServer(JSONObject metrics) {
        //   String runtimeServerUrl = "https://lotuss366.com/pratibandhAPI/run_time_decteced_metrics.php";
        NetworkHelper.sendPostRequest(context, runtimeServerUrl, metrics.toString());
    }

//    public void onStop(Activity activity) {
//        if (screenshotDetection != null) {
//            screenshotDetection.disableScreenshotDetection();
//        }
//    }

//    private void exitApp(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.finishAndRemoveTask();
//        } else {
//            activity.finishAffinity();
//        }
//    }

//    private void showToast(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
//
//    private void showToast(String message) {
//        new Handler(Looper.getMainLooper()).post(() -> {
//             Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
////
//        });
//    }



}
