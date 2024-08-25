//package com.vaptlab.pratibandhsdk;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import android.app.Activity;
//
//
//public class NetworkHelper {
//
////    public static void sendPostRequest(Context context, String urlString, String jsonData) {
////        new Thread(() -> {
////            HttpURLConnection urlConnection = null;
////            try {
////                URL url = new URL(urlString);
////                urlConnection = (HttpURLConnection) url.openConnection();
////                urlConnection.setRequestMethod("POST");
////                urlConnection.setRequestProperty("Content-Type", "application/json");
////                urlConnection.setDoOutput(true);
////                urlConnection.setDoInput(true);
////
////                OutputStream os = urlConnection.getOutputStream();
////                os.write(jsonData.getBytes("UTF-8"));
////                os.close();
////
////                int responseCode = urlConnection.getResponseCode();
////
////                if (responseCode == HttpURLConnection.HTTP_OK) {
////                    Log.d("NetworkHelper", "POST request successful.");
////                    ((Activity) context).runOnUiThread(() ->
////                            Toast.makeText(context, "POST request successful.", Toast.LENGTH_SHORT).show()
////                    );
////                } else {
////                    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
////                    ((Activity) context).runOnUiThread(() ->
////                            Toast.makeText(context, "POST request failed with response code: " + responseCode, Toast.LENGTH_SHORT).show()
////                    );
////                }
////            } catch (Exception e) {
////                String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
////                Log.e("NetworkHelper", errorMessage);
////                ((Activity) context).runOnUiThread(() ->
////                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
////                );
////            } finally {
////                if (urlConnection != null) {
////                    urlConnection.disconnect();
////                }
////            }
////        }).start();
////    }
//
//    public static void sendPostRequest(Context context, String urlString, String jsonData) {
//        new Thread(() -> {
//            HttpURLConnection urlConnection = null;
//            try {
//                URL url = new URL(urlString);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//
//                OutputStream os = urlConnection.getOutputStream();
//                os.write(jsonData.getBytes("UTF-8"));
//                os.close();
//
//                int responseCode = urlConnection.getResponseCode();
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    Log.d("NetworkHelper", "POST request successful.");
//                    if (context instanceof Activity) {
//                        ((Activity) context).runOnUiThread(() ->
//                                Toast.makeText(context, "POST request successful.", Toast.LENGTH_SHORT).show()
//                        );
//                    } else {
//                        Log.d("NetworkHelper", "Context is not an instance of Activity.");
//                    }
//                } else {
//                    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
//                    if (context instanceof Activity) {
//                        ((Activity) context).runOnUiThread(() ->
//                                Toast.makeText(context, "POST request failed with response code: " + responseCode, Toast.LENGTH_SHORT).show()
//                        );
//                    }
//                }
//            } catch (Exception e) {
//                String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
//                Log.e("NetworkHelper", errorMessage);
//                if (context instanceof Activity) {
//                    ((Activity) context).runOnUiThread(() ->
//                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                    );
//                }
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//            }
//        }).start();
//    }
//
//    public static String sendGetRequest(Context context, String urlString) {
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        try {
//            URL url = new URL(urlString);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuilder buffer = new StringBuilder();
//            if (inputStream == null) {
//                Toast.makeText(context, "No data received from server", Toast.LENGTH_SHORT).show();
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line).append("\n");
//            }
//
//            if (buffer.length() == 0) {
//                Toast.makeText(context, "Empty response from server", Toast.LENGTH_SHORT).show();
//                return null;
//            }
//
//            return buffer.toString().trim();
//        } catch (Exception e) {
//            Log.e("NetworkHelper", "Error fetching data from server: " + e.getMessage());
//            Toast.makeText(context, "Error fetching data from server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            return null;
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (Exception e) {
//                    Log.e("NetworkHelper", "Error closing reader: " + e.getMessage());
//                    Toast.makeText(context, "Error closing reader: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//    public static void sendChecksumToServer(Context context, String checksum) {
//        // Assuming you have a URL to send the checksum to
//        String url = "https://sundar.com/checkumsave";
//        // Send the checksum as JSON data
//        String jsonData = "{\"checksum\":\"" + checksum + "\"}";
//        sendPostRequest(context, url, jsonData);
//    }
//}

package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONException;

public class NetworkHelper {
    public interface PostRequestCallback {
        void onSuccess(String response);
        void onFailure(String error);
    }


    public static void sendPostRequest(Context context, String urlString, String jsonData) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStream os = urlConnection.getOutputStream();
                os.write(jsonData.getBytes("UTF-8"));
                os.close();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("NetworkHelper", "POST request successful.");
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() ->
                                        Log.e("NetworkHelper", "POST request failed with response code: ")

                           //  Toast.makeText(context, "POST request successful.", Toast.LENGTH_SHORT).show()
                        );
                    } else {
                        Log.d("NetworkHelper", "Context is not an instance of Activity, cannot show toast.");
                    }
                } else {
                    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() ->
                            //    Log.e("NetworkHelper", "POST request failed with response code: " );
                                        Log.e("NetworkHelper", "POST request failed with response code: ")
                   //   Toast.makeText(context, "POST request failed with response code: " + responseCode, Toast.LENGTH_SHORT).show()
                        );
                    } else {
                        Log.e("NetworkHelper", "Context is not an instance of Activity, cannot show toast.");
                    }
                }
            } catch (Exception e) {
                String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
                Log.e("NetworkHelper", errorMessage);
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() ->
                        //    Log.e("NetworkHelper", "POST request failed with response code: " );
                                    Log.e("NetworkHelper", "POST request failed with response code: ")
              //      Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    );
                } else {
                    Log.e("NetworkHelper", "Context is not an instance of Activity, cannot show toast.");
                }
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }



//    public static void postRequest(Context context, String urlString, String jsonData, PostRequestCallback callback) {
//        new Thread(() -> {
//            HttpURLConnection urlConnection = null;
//            StringBuilder response = new StringBuilder();
//            try {
//                URL url = new URL(urlString);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//
//                // Write JSON data to the output stream
//                OutputStream os = urlConnection.getOutputStream();
//                os.write(jsonData.getBytes("UTF-8"));
//                os.close();
//
//                int responseCode = urlConnection.getResponseCode();
//
//                // Read the response from the server
//                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    Log.d("NetworkHelper", "POST request successful.");
//                    if (callback != null) {
//                        runOnUiThread(context, () -> callback.onSuccess(response.toString()));
//                    }
//                } else {
//                    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
//                    if (callback != null) {
//                        runOnUiThread(context, () -> callback.onFailure("POST request failed with response code: " + responseCode));
//                    }
//                }
//            } catch (Exception e) {
//                String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
//                Log.e("NetworkHelper", errorMessage);
//                if (callback != null) {
//                    runOnUiThread(context, () -> callback.onFailure(errorMessage));
//                }
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//            }
//        }).start();
//    }

//    public static void postRequest(Context context, String urlString, String jsonData, PostRequestCallback callback) {
//        new Thread(() -> {
//            HttpURLConnection urlConnection = null;
//            StringBuilder response = new StringBuilder();
//            try {
//                URL url = new URL(urlString);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//
//                // Write JSON data to the output stream
//                OutputStream os = urlConnection.getOutputStream();
//                os.write(jsonData.getBytes("UTF-8"));
//                os.close();
//
//                int responseCode = urlConnection.getResponseCode();
//                Log.d("NetworkHelper", "Response Code: " + responseCode);
//
//                // Read the response from the server
//                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    Log.d("NetworkHelper", "POST request successful. Response: " + response.toString());
//                    if (callback != null) {
//                        runOnUiThread(context, () -> callback.onSuccess(response.toString()));
//                    }
//                } else {
//                    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
//                    if (callback != null) {
//                        runOnUiThread(context, () -> callback.onFailure("POST request failed with response code: " + responseCode));
//                    }
//                }
//            } catch (Exception e) {
//                String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
//                Log.e("NetworkHelper", errorMessage);
//                if (callback != null) {
//                    runOnUiThread(context, () -> callback.onFailure(errorMessage));
//                }
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//            }
//        }).start();
//    }
/*
    public static void postRequest(Context context, String urlString, String jsonData, PostRequestCallback callback) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                // Write JSON data to the output stream
                OutputStream os = urlConnection.getOutputStream();
                os.write(jsonData.getBytes("UTF-8"));
                os.close();

                int responseCode = urlConnection.getResponseCode();

                // Read the response from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String jsonResponse = response.toString();
                    Log.d("NetworkHelper", "POST request successful. Response: " + jsonResponse);

                    Toast.makeText(context, "HTTP POST hash:"+jsonResponse, Toast.LENGTH_SHORT).show();

                    // Extract signature_hash from JSON response
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String signatureHash = jsonObject.optString("signature_hash", null);
                    Toast.makeText(context, "HTTP POST hash:"+signatureHash, Toast.LENGTH_SHORT).show();

                    // Use the extracted signature_hash
                    if (callback != null) {
                        runOnUiThread(context, () -> callback.onSuccess(signatureHash));
                    }
                } else {
                    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                    if (callback != null) {
                        runOnUiThread(context, () -> callback.onFailure("POST request failed with response code: " + responseCode));
                    }
                }
            } catch (Exception e) {
                String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
                Log.e("NetworkHelper", errorMessage);
                if (callback != null) {
                    runOnUiThread(context, () -> callback.onFailure(errorMessage));
                }
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }
*/
    /*
   public static void postRequest(Context context, String urlString, String jsonData, PostRequestCallback callback) {
    new Thread(() -> {
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            // Write JSON data to the output stream
            OutputStream os = urlConnection.getOutputStream();
            os.write(jsonData.getBytes("UTF-8"));
            os.close();

            int responseCode = urlConnection.getResponseCode();

            // Read the response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String jsonResponse = response.toString();
                Log.d("NetworkHelper", "POST request successful. Response: " + jsonResponse);

                // Extract signature_hash from JSON response
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String signatureHash = jsonObject.optString("signature_hash", null);
Log.d("NetworkHelper", "hash networkhelp: " + signatureHash);
                // Use runOnUiThread for Toast messages
                runOnUiThread(context, () -> {
                    Toast.makeText(context, "HTTP POST hash: " + signatureHash, Toast.LENGTH_SHORT).show();
                    if (callback != null) {
                        callback.onSuccess(signatureHash);
                    }
                });

            } else {
                Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                runOnUiThread(context, () -> {
                    if (callback != null) {
                        callback.onFailure("POST request failed with response code: " + responseCode);
                    }
                });
            }
        } catch (Exception e) {
            String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            Log.e("NetworkHelper", errorMessage);
            runOnUiThread(context, () -> {
                if (callback != null) {
                    callback.onFailure(errorMessage);
                }
            });
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }).start();
}
*/
    /*
   public static void postRequest(Context context, String urlString, String jsonData, PostRequestCallback callback) {
    new Thread(() -> {
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.write(jsonData.getBytes("UTF-8"));
            os.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d("NetworkHelper", "Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("NetworkHelper", "POST request successful. Response: " + response.toString());

                // Extract signature_hash from JSON response
                JSONObject jsonObject = new JSONObject(response.toString());
                String signatureHash = jsonObject.optString("signature_hash", null);

                // Callback for success
                runOnUiThread(context, () -> {
                    Log.d("NetworkHelper", "Running on UI thread - onSuccess");
                    if (callback != null) {
                        callback.onSuccess(signatureHash);
                    }
                });
            } else {
                Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                runOnUiThread(context, () -> {
                    Log.d("NetworkHelper", "Running on UI thread - onfail");
                    if (callback != null) {
                        callback.onFailure("POST request failed with response code: " + responseCode);
                    }
                });
            }
        } catch (Exception e) {
            String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            Log.e("NetworkHelper", errorMessage);
            runOnUiThread(context, () -> {
                Log.d("NetworkHelper", "Running on UI thread - onerroe");
                if (callback != null) {
                    callback.onFailure(errorMessage);
                }
            });
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }).start();
}
*/
//   public static void postRequest(Context context, String urlString, String jsonData, PostRequestCallback callback) {
//    new Thread(() -> {
//        HttpURLConnection urlConnection = null;
//        StringBuilder response = new StringBuilder();
//        try {
//            URL url = new URL(urlString);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type", "application/json");
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
//
//            // Write JSON data to the output stream
//            OutputStream os = urlConnection.getOutputStream();
//            os.write(jsonData.getBytes("UTF-8"));
//            os.close();
//
//            int responseCode = urlConnection.getResponseCode();
//            Log.d("NetworkHelper", "Response Code: " + responseCode);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                Log.d("NetworkHelper", "POST request successful. Response: " + response.toString());
//
//                JSONObject jsonObject = new JSONObject(response.toString());
//                String signatureHash = jsonObject.optString("signature_hash", null);
//
//                Log.d("NetworkHelper", "Extracted Signature Hash: " + signatureHash);
//
//                runOnUiThread(context, () -> {
//                    Log.d("NetworkHelper", "Running on UI thread - onSuccess");
//                    if (callback != null) {
//                        callback.onSuccess(signatureHash);
//                    }
//                });
//            } else {
//                Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
//                runOnUiThread(context, () -> {
//                    Log.d("NetworkHelper", "Running on UI thread - onFailure");
//                    if (callback != null) {
//                        callback.onFailure("POST request failed with response code: " + responseCode);
//                    }
//                });
//            }
//        } catch (Exception e) {
//            String errorMessage = "Error in sending POST request: " + e.getClass().getSimpleName() + " - " + e.getMessage();
//            Log.e("NetworkHelper", errorMessage);
//            runOnUiThread(context, () -> {
//                Log.d("NetworkHelper", "Running on UI thread - onFailure due to exception");
//                if (callback != null) {
//                    callback.onFailure(errorMessage);
//                }
//            });
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
//    }).start();
//}
//
//    private static void runOnUiThread(Context context, Runnable action) {
//        if (context instanceof Activity) {
//            ((Activity) context).runOnUiThread(action);
//        } else {
//            Log.e("NetworkHelper", "Context is not an instance of Activity, cannot execute action on UI thread.");
//        }
//    }

    public static void postRequest(Context context, String urlString, String requestBody, PostRequestCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                // Create the URL object
                URL url = new URL(urlString);

                // Open a connection to the server
                connection = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                connection.setRequestMethod("POST");

                // Set headers if needed (e.g., Content-Type)
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");

                // Enable input and output streams
                connection.setDoOutput(true);

                // Write the request body to the output stream
                connection.getOutputStream().write(requestBody.getBytes("UTF-8"));

                // Get the response code
                int responseCode = connection.getResponseCode();
                Log.d("NetworkHelper", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String response = convertStreamToString(inputStream);

                    Log.d("NetworkHelper", "POST request successful. Response: " + response);
                    if (callback != null) {
                        callback.onSuccess(response);
                    }
                } else {
                    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                    if (callback != null) {
                        callback.onFailure("Failed with response code: " + responseCode);
                    }
                }

            } catch (Exception e) {
                Log.e("NetworkHelper", "Exception in POST request", e);
                if (callback != null) {
                    callback.onFailure("Exception: " + e.getMessage());
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }



    public static String sendGetRequest(Context context, String urlString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() ->
                        //    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                                    Log.e("NetworkHelper", "POST request failed with response code: ")
              //    Toast.makeText(context, "No data received from server", Toast.LENGTH_SHORT).show()
                    );
                }
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(() ->
                        //    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                                    Log.e("NetworkHelper", "POST request failed with response code: ")
                  //     Toast.makeText(context, "Empty response from server", Toast.LENGTH_SHORT).show()
                    );
                }
                return null;
            }

            return buffer.toString().trim();
        } catch (Exception e) {
            Log.e("NetworkHelper", "Error fetching data from server: " + e.getMessage());
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() ->
                    //    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                                Log.e("NetworkHelper", "POST request failed with response code: ")
         //  Toast.makeText(context, "Error fetching data from server: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e("NetworkHelper", "Error closing reader: " + e.getMessage());
                    if (context instanceof Activity) {
                        ((Activity) context).runOnUiThread(() ->
                            //    Log.e("NetworkHelper", "POST request failed with response code: " + responseCode);
                                        Log.e("NetworkHelper", "POST request failed with response code: ")
                //   Toast.makeText(context, "Error closing reader: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            }
        }
    }

    public static void sendChecksumToServer(Context context, String checksum) {
        String url = "https://sundar.com/checkumsave";
        String jsonData = "{\"checksum\":\"" + checksum + "\"}";
        sendPostRequest(context, url, jsonData);
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() ->
                   Log.e("NetworkHelper", "POST request failed with response code: ")

         //   Toast.makeText(context, "Checksum sent to server.", Toast.LENGTH_SHORT).show()
            );
        } else {
            Log.d("NetworkHelper", "Context is not an instance of Activity, cannot show toast.");
        }
    }

  }