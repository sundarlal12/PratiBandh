package com.vaptlab.pratibandhsdk;
//import com.vaptlab.pratibandhsdk.MacAddressDetection;

import android.content.Context;
import org.json.JSONObject;
import android.Manifest;
import android.app.Activity;
import java.util.concurrent.Future;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import android.util.Log;

import java.util.Map;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;
//private ExecutorService executorService;
public class SecuritySDK {
String exp="1";
    String curt="2";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static SecuritySDK instance;
    private Context context;
    private String serverUrl;
    private String runtimeclone;
    private boolean isScreenshotDetected = false;

    private String runtimeServerUrl;



    private FileObserver screenshotObserver;

    private ScreenshotDetection screenshotDetection;

    //  private static final String EXPECTED_SIGNATURE = "your-expected-signature"; // Replace with actual expected signature
    private static final String EXPECTED_CLASS_CHECKSUM = "your-expected-checksum"; // Replace with actual expected checksum

   private ExecutorService executorService;



    private SecuritySDK(Context context) {
        this.context = context.getApplicationContext();
        this.serverUrl = "https://lotuss366.com/pratibandhAPI/metrics.php";
        this.runtimeServerUrl = "https://lotuss366.com/pratibandhAPI/run_time_decteced_metrics.php";

        this.runtimeclone="https://lotuss366.com/pratibandhAPI/Set_orginial_signature.php";

        this.executorService = Executors.newFixedThreadPool(4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            screenshotDetection = new ScreenshotDetection(context, this);
        }

        screenshotDetection = new ScreenshotDetection(this.context, this);
        screenshotDetection.enableScreenshotDetection();

//        ScreenshotDetection screenshotDetection = new ScreenshotDetection(activity);
//        screenshotDetection.enableScreenshotDetection();

    }

    public static SecuritySDK getInstance(Context context) {
        if (instance == null) {
            instance = new SecuritySDK(context);
        }
        return instance;
    }

    public static void initializeAndCheckSecurity(Activity activity) {
        SecuritySDK securitySDK = new SecuritySDK(activity);
        securitySDK.performSecurityCheck(activity);

//        if (securitySDK.screenshotDetection != null) {
//            securitySDK.screenshotDetection.enableScreenshotDetection(activity);
//        }


    }

    private void performSecurityCheck(Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_REQUEST_CODE);
        } else {
            proceedWithSecurityCheck(activity);
        }




    }
    public void retrieveExpectedSignatureHash(Context context) {
        SignatureUtils.getExpectedSignatureHash(
                context,
                context.getPackageName(),  // Use your package name
                SignatureUtils.getAppName(context), // Retrieve the app name
                new SignatureUtils.SignatureHashCallback() {
                    @Override
                    public void onSuccess(String signatureHash) {
                        // Handle the retrieved signature hash
                        Log.d("Expected Signature Hash",signatureHash);
                        // Toast.makeText(context, "Expected Signature Hash: " + signatureHash, Toast.LENGTH_LONG).show();
                        // You can now compare this hash with the current signature hash
                    }

                    @Override
                    public void onFailure(String error) {
                        // Handle the failure
                        Log.d("Expected Signature Hash","null");
                        // Toast.makeText(context, "Failed to get signature hash: " + error, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }



    private void proceedWithSecurityCheck(Activity activity) {
        JSONObject securityMetrics = new JSONObject();
        boolean securityIssueDetected = false;
        boolean securityIssueDetected1 = false;

        try {
            // Get package name, app name, and device information
            String packageName = context.getPackageName();
            String appName = getAppName(context);
            String deviceId = getDeviceId(context);
            String deviceName = getDeviceName();
            String imei = getIMEINumber(context);
            String deviceType = getDeviceType();
            String sig_hash=SignatureUtils.generateCurrentSignatureHash(context);
            retrieveExpectedSignatureHash(context);
            // String deviceInfo = getDeviceInfo();
            JSONObject deviceInfo = getDeviceInfo();
            // Add package name, app name, and device information to metrics
           // String mac = MacAddressDetection.getMacAddress(context);

          //  securityMetrics.put("mac-address", mac);
            securityMetrics.put("Signature_Hash", sig_hash);

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

                securityIssueDetected = true;
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

                securityIssueDetected = true;
            }

            //code


            if (RootDetection.isDeviceRooted()) {
                Log.d("issue#1", "Rooted device");
//                showToast("Root detection detected!");
                securityMetrics.put("isRooted", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isRooted", false);
            }
//            showToast("Emulator :"+EmulatorDetection.isEmulator());
            if (EmulatorDetection.isEmulator()) {
                Log.d("issue#2", "Emulator detected");
//                showToast("Emulator detected!");
                securityMetrics.put("isEmulator", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isEmulator", false);
            }

            if (UsbDebuggingDetection.isUsbDebuggingEnabled(context)) {
                Log.d("issue#3", "USB debugging enabled");
//                showToast("USB debugging detected!");
                securityMetrics.put("isUsbDebuggingEnabled", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isUsbDebuggingEnabled", false);
            }

            if (DeveloperOptionsDetection.isDeveloperOptionsEnabled(context)) {
                Log.d("issue#4", "Developer options enabled");
//                showToast("Developer options enabled!");
                securityMetrics.put("isDeveloperOptionsEnabled", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isDeveloperOptionsEnabled", false);
            }

            if (!VerifyInstallerDetection.isInstalledFromValidSource(context)) {
                Log.d("issue#5", "App not installed from a valid source");
//                showToast("App not installed from a valid source!");
                securityMetrics.put("isInstalledFromValidSource", false);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isInstalledFromValidSource", true);
            }

            if (AppCloneDetection.isAppCloned(context,appName)) {
                Log.d("issue#6", "App clone detected");
//                showToast("App clone detected!");
                securityMetrics.put("isAppCloned", true);
                securityIssueDetected = true;
                securityIssueDetected1 = true;

                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);

            } else {
                securityMetrics.put("isAppCloned", false);
            }

            //code
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
                        }
                        // Send metrics to server after setting the value
                        sendMetricsToServer(securityMetrics);

                    } catch (Exception e) {
                        Log.e("tes", "Error creating JSON object", e);
                    }
                }
            });


            //code

            if (Debug.isDebuggerConnected()) {
                Log.d("issue#8", "Debugging detected");
//                showToast("Debugging detected!");
                securityMetrics.put("isDebugging", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isDebugging", false);
            }

//            if (hasAppBeenTamperedWith()) {
//                Log.d("issue#9", "App tampering detected");
////                showToast("App tampering detected!");
//                securityMetrics.put("isTampered", true);
//                securityIssueDetected = true;
//            } else {
//                securityMetrics.put("isTampered", false);
//            }

            if (isVPNEnabled()) {
                Log.d("issue#10", "VPN detected");
//                showToast("VPN detected!");
                securityMetrics.put("isVPNEnabled", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isVPNEnabled", false);
            }
//            if (isProxyEnabled()) {
//                Log.d("issue#10", "Proxy enabled");
//                showToast("Proxy detected!");
//                securityMetrics.put("isProxyEnabled", true);
//                securityIssueDetected = true;
//            } else {
//                securityMetrics.put("isProxyEnabled", false);
//            }

            if (ProxyDetection.isProxyEnabled(context)) {
                Log.d("issue#10", "Proxy enabled");
//                showToast("Proxy detected!");
                securityMetrics.put("isProxyEnabled", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isProxyEnabled", false);
            }

            if (isScreenRecording(context)) {
                Log.d("issue#11", "Screen recording detected");
//                showToast("Screen recording detected!");
                securityMetrics.put("isScreenRecording", true);
                securityIssueDetected = true;
                securityIssueDetected1 = true;

                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);
            } else {
                securityMetrics.put("isScreenRecording", false);
            }


//            if (isScreenshotDetected()) {
//                Log.d("issue#12", "Screenshot detected");
//                showToast("Screenshot detected!");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
//            } else {
//                securityMetrics.put("isScreenshotDetected", false);
//            }
//
//            if (screenshotDetection != null && screenshotDetection.isScreenshotDetected()) {
//                Log.d("SecuritySDK", "Screenshot detected");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
//                showToast("screenshot detected!");
//              //  sendMetricsToServer(securityMetrics);
//            }


//            if (screenshotDetection.isScreenshotDetected()) {
//                Log.d("SecuritySDK", "Screenshot detected");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
//                sendRuntimeMetricsToServer(securityMetrics);
//
//                //  sendMetricsToServer(securityMetrics);
//            }
//            if (screenshotDetection.isScreenshotDetected()) {
//                Log.d("SecuritySDK", "Screenshot detected");
//                securityMetrics.put("isScreenshotDetected", true);
//                securityIssueDetected = true;
//                securityIssueDetected1 = true;
//                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);
//            }


            if (MockLocationDetection.isMockLocationEnabled(context)) {
                Log.d("issue#13", "Mock location detected");
//                showToast("Mock location detected!");
                securityMetrics.put("isMockLocationEnabled", true);
                securityIssueDetected = true;
            } else {
                securityMetrics.put("isMockLocationEnabled", false);
            }

            if (securityIssueDetected) {
                sendMetricsToServer(securityMetrics);
                new Handler().postDelayed(() -> exitApp(activity), 20000); // Delay of 20 seconds
            }
            if (securityIssueDetected1) {
                sendRuntimeMetricsAndCloseApp(securityMetrics, activity);
                sendMetricsToServerruntime(securityMetrics);
                new Handler().postDelayed(() -> exitApp(activity), 20000); // Delay of 20 seconds
            }


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

    private void startScreenshotDetection() {
        File screenshotDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Screenshots");
        if (screenshotDir.exists()) {
            screenshotObserver = new FileObserver(screenshotDir.getPath(), FileObserver.CREATE) {
                @Override
                public void onEvent(int event, String path) {
                    if (event == FileObserver.CREATE) {
                        Log.d("SecuritySDK", "Screenshot detected: " + path);
                        isScreenshotDetected = true;
                    }
                }
            };
            screenshotObserver.startWatching();
        }
    }

    private boolean isScreenshotDetected() {
        return isScreenshotDetected;
    }

    public void stopScreenshotDetection() {
        if (screenshotObserver != null) {
            screenshotObserver.stopWatching();
        }
    }

    public void onScreenshotDetected() {
        JSONObject securityMetrics = new JSONObject();
        try {
            securityMetrics.put("isScreenshotDetected", true);
            sendMetricsToServer(securityMetrics);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void onStop(Activity activity) {
//        screenshotDetection.disableScreenshotDetection();
//    }



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

//    private boolean isAppSignatureValid(Context context) {
//        try {
//            PackageManager pm = context.getPackageManager();
//            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//            for (android.content.pm.Signature signature : packageInfo.signatures) {
//                String currentSignature = new String(signature.toByteArray());
//                return EXPECTED_SIGNATURE.equals(currentSignature);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    private boolean isAppSignatureValid(Context context) {
//        final boolean[] isValid = {false}; // Use an array to hold the result
//        boolean valid=false;
//        // Retrieve the expected signature hash from the server
//        SignatureUtils.getExpectedSignatureHash(context, context.getPackageName(), SignatureUtils.getAppName(context), new SignatureUtils.SignatureHashCallback() {
//            @Override
//            public void onSuccess(String expectedSignatureHash) {
//                // Generate the current signature hash
//                String currentSignatureHash = SignatureUtils.generateCurrentSignatureHash(context);
//
//  Log.d("tes", "current: " + currentSignatureHash);
//  Log.d("tsesr", "expect: " + expectedSignatureHash);
// curt=currentSignatureHash;
// exp=expectedSignatureHash;
//
//                // Compare the hashes
//                isValid[0] = expectedSignatureHash != null && expectedSignatureHash.equals(currentSignatureHash);
// Log.d("tes", "signature+check: " + isValid[0]);
//   // valid=isValid[0];
//              //  return isValid[0];
//            }
//
//            @Override
//            public void onFailure(String error) {
//                // Handle the error (e.g., log it or show a toast)
//                Log.e("SecuritySDK", "Failed to get expected signature hash: " + error);
//            }
//        });
//
//        Log.d("tes", "signature+valid: " + isValid[0]);
//        // Return the validity status after the asynchronous operation completes
//        return isValid[0];
//       // return valid;
//    }


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
            public void onSuccess(String expectedSignatureHash) {
                // Generate the current signature hash
                String currentSignatureHash = SignatureUtils.generateCurrentSignatureHash(context);

                Log.d("tes", "current: " + currentSignatureHash);
                Log.d("tesr", "expect: " + expectedSignatureHash);

                // Compare the hashes
                boolean isValid = expectedSignatureHash != null && expectedSignatureHash.equals(currentSignatureHash);
                Log.d("tes", "signature check: " + isValid);

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

    private String getIMEINumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "Permission not granted";
        }
        return telephonyManager.getDeviceId();  // This method is deprecated in Android Q (API 29+)
    }




    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Activity activity) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedWithSecurityCheck(activity);
            } else {
                showToast("Permission denied. Some security features may not work.");
            }
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
        try {
            deviceInfo.put("brand", Build.BRAND);
            deviceInfo.put("model", Build.MODEL);
            deviceInfo.put("manufacturer", Build.MANUFACTURER);
            deviceInfo.put("android_version", Build.VERSION.RELEASE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }
//    private boolean isProxyEnabled() {
//        String proxyAddress = System.getProperty("http.proxyHost");
//        String proxyPort = System.getProperty("http.proxyPort");
//        return proxyAddress != null && proxyPort != null;
//    }

//    private boolean isProxyEnabled() {
//        try {
//            List<Proxy> proxyList = ProxySelector.getDefault().select(new java.net.URI("http://www.google.com"));
//            for (Proxy proxy : proxyList) {
//                InetSocketAddress addr = (InetSocketAddress) proxy.address();
//                if (addr != null) {
//                    String proxyAddress = addr.getHostName();
//                    int proxyPort = addr.getPort();
//                    return true; // Proxy is enabled
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false; // No proxy detected
//    }



    public void sendMetricsToServer(JSONObject metrics) {
        NetworkHelper.sendPostRequest(context, serverUrl, metrics.toString());
    }
    public void sendMetricsToServerruntime(JSONObject metrics) {
        NetworkHelper.sendPostRequest(context, runtimeclone, metrics.toString());
    }

    private void sendRuntimeMetricsAndCloseApp(JSONObject metrics, Activity activity) {
        sendRuntimeMetricsToServer(metrics);
        new Handler().postDelayed(() -> exitApp(activity), 20000); // Close app after 20 seconds
    }


    public void sendRuntimeMetricsToServer(JSONObject metrics) {
        //   String runtimeServerUrl = "https://lotuss366.com/pratibandhAPI/run_time_decteced_metrics.php";
        NetworkHelper.sendPostRequest(context, runtimeServerUrl, metrics.toString());
    }

    public void onStop(Activity activity) {
        if (screenshotDetection != null) {
            screenshotDetection.disableScreenshotDetection();
        }
    }

    private void exitApp(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAndRemoveTask();
        } else {
            activity.finishAffinity();
        }
    }

//    private void showToast(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
private void showToast(String message) {
    new Handler(Looper.getMainLooper()).post(() -> {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    });
}


}
