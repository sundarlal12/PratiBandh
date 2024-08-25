package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SignatureUtils {

    private static final String TAG = "SignatureUtils";
    private static final String GET_SIGNATURE_URL = "https://lotuss366.com/pratibandhAPI/Get_orginial_signature.php";
    private static final String SET_SIGNATURE_URL = "https://lotuss366.com/pratibandhAPI/Set_orginial_signature.php";

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static Context applicationContext;

    // Initialize with application context
    public static void initialize(Context context) {
        applicationContext = context.getApplicationContext();
    }

    // Method to generate the current signature hash
    public static String generateCurrentSignatureHash(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] signature = packageInfo.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(signature);

            StringBuilder hexString = new StringBuilder();
            for (byte aDigest : digest) {
                String hex = Integer.toHexString(0xFF & aDigest);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String signatureHash = hexString.toString();
            String appName = getAppName(context);

            // Call to set the original signature hash
            setOriginalSignatureHash(applicationContext, context.getPackageName(), appName, signatureHash);

            return signatureHash;
        } catch (Exception e) {
            Log.e(TAG, "Failed to generate current signature hash", e);
            return null;
        }
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return (String) packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(context.getPackageName(), 0));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to retrieve app name", e);
            return "Unknown App";
        }
    }

    // Synchronous method to get the expected signature hash using Callable and Future
//    public static String getExpectedSignatureHashSync(Context context, String packageName, String name) {
//        Future<String> future = executorService.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                final CountDownLatch latch = new CountDownLatch(1);
//                final String[] result = new String[1];
//                final String[] error = new String[1];
//
//                getExpectedSignatureHash(context, packageName, name, new SignatureHashCallback() {
//                    @Override
//                    public void onSuccess(String signatureHash) {
//                        Log.d(TAG, "Response from signutils: " + signatureHash);
//                        result[0] = signatureHash;
//                        latch.countDown();
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        Log.d(TAG, "Response from signutils: " + error);
//                   //     error[0] = error;
//                        latch.countDown();
//                    }
//                });
//
//                latch.await(); // Wait until the network operation completes
//
//                if (error[0] != null) {
//                    throw new RuntimeException("Error fetching expected signature hash: " + error[0]);
//                }
//
//                return result[0];
//            }
//        });
//
//        try {
//            return future.get(); // Get the result from the Future
//        } catch (InterruptedException | ExecutionException e) {
//            Log.e(TAG, "Failed to get expected signature hash", e);
//            return null;
//        }
//    }

    public static void getExpectedSignatureHash(Context context, String packageName, String name, SignatureHashCallback callback) {
        try {
            // Prepare the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("package_name", packageName);
            requestBody.put("app_name", name);

            // Make the network request
            NetworkHelper.postRequest(applicationContext, GET_SIGNATURE_URL, requestBody.toString(), new NetworkHelper.PostRequestCallback() {
                @Override
                public void onSuccess(String response) {
                    // Handle the success response
                    Log.d(TAG, "Response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String signatureHash = jsonResponse.optString("signature_hash");
                        if (callback != null) {
                            callback.onSuccess(signatureHash);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing response", e);
                        if (callback != null) {
                            callback.onFailure("Error parsing response: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(String error) {
                    // Handle the failure response
                    Log.e(TAG, "Error: " + error);
                    if (callback != null) {
                        callback.onFailure(error);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error fetching expected signature hash", e);
            if (callback != null) {
                callback.onFailure("Exception: " + e.getMessage());
            }
        }
    }

    public static void setOriginalSignatureHash(Context context, String packageName, String name, String signatureHash) {
        try {
            // Prepare the request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("package_name", packageName);
            requestBody.put("app_name", name);
            requestBody.put("Signature_Hash", signatureHash);

            // Make the network request
            NetworkHelper.sendPostRequest(applicationContext, SET_SIGNATURE_URL, requestBody.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error setting original signature hash", e);
        }
    }

    public interface SignatureHashCallback {
        void onSuccess(String signatureHash);
        void onFailure(String error);
    }
}
