package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyDetection {

    /**
     * Combines multiple methods to detect if a proxy is enabled on the device and retrieves the proxy details.
     *
     * @param context The context of the calling component.
     * @return A map containing proxy details if a proxy is detected, otherwise an empty map.
     */
    public static Map<String, String> getProxyDetails(Context context) {
        Map<String, String> proxyDetails = new HashMap<>();

        // Check system-wide proxy settings
        Map<String, String> systemProxyDetails = getSystemProxyDetails();
        if (!systemProxyDetails.isEmpty()) {
            proxyDetails.putAll(systemProxyDetails);
        }

        // Check network proxy settings
        Map<String, String> networkProxyDetails = getNetworkProxyDetails(context);
        if (!networkProxyDetails.isEmpty()) {
            proxyDetails.putAll(networkProxyDetails);
        }

        return proxyDetails;
    }

    /**
     * Checks if a system-wide proxy is enabled and retrieves the details.
     *
     * @return A map containing system proxy details if a proxy is enabled, otherwise an empty map.
     */
    public static Map<String, String> getSystemProxyDetails() {
        Map<String, String> proxyDetails = new HashMap<>();

        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");

        String httpsProxyHost = System.getProperty("https.proxyHost");
        String httpsProxyPort = System.getProperty("https.proxyPort");

        if (proxyHost != null && proxyPort != null) {
            proxyDetails.put("system_http_proxy_ip", proxyHost);
            proxyDetails.put("system_http_proxy_port", proxyPort);
            Log.d("ProxyDetection", "System HTTP Proxy: " + proxyHost + ":" + proxyPort);
        }

        if (httpsProxyHost != null && httpsProxyPort != null) {
            proxyDetails.put("system_https_proxy_ip", httpsProxyHost);
            proxyDetails.put("system_https_proxy_port", httpsProxyPort);
            Log.d("ProxyDetection", "System HTTPS Proxy: " + httpsProxyHost + ":" + httpsProxyPort);
        }

        return proxyDetails;
    }


    /**
     * Checks if a network proxy is enabled and retrieves the details.
     *
     * @param context The context of the calling component.
     * @return A map containing network proxy details if a proxy is enabled, otherwise an empty map.
     */
    public static Map<String, String> getNetworkProxyDetails(Context context) {
        Map<String, String> proxyDetails = new HashMap<>();

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                List<Proxy> proxyList = ProxySelector.getDefault().select(new java.net.URI("http://www.google.com"));
                for (Proxy proxy : proxyList) {
                    InetSocketAddress addr = (InetSocketAddress) proxy.address();
                    if (addr != null) {
                        String proxyHost = addr.getHostName();
                        int proxyPort = addr.getPort();
                        proxyDetails.put("network_proxy_ip", proxyHost);
                        proxyDetails.put("network_proxy_port", String.valueOf(proxyPort));
                        Log.d("ProxyDetection", "Network Proxy: " + proxyHost + ":" + proxyPort);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return proxyDetails;
    }

    /**
     * Checks if a system-wide proxy is enabled by examining system properties.
     *
     * @return True if a system-wide proxy is enabled, otherwise false.
     */
    public static boolean isSystemProxyEnabled() {
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");

        String httpsProxyHost = System.getProperty("https.proxyHost");
        String httpsProxyPort = System.getProperty("https.proxyPort");

        boolean httpProxyEnabled = proxyHost != null && proxyPort != null;
        boolean httpsProxyEnabled = httpsProxyHost != null && httpsProxyPort != null;

        return httpProxyEnabled || httpsProxyEnabled;
    }



    /**
     * Checks if a network proxy is enabled by examining the network's proxy settings.
     *
     * @param context The context of the calling component.
     * @return True if a network proxy is detected, otherwise false.
     */
    public static boolean isNetworkProxyEnabled(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                List<Proxy> proxyList = ProxySelector.getDefault().select(new java.net.URI("http://www.google.com"));
                for (Proxy proxy : proxyList) {
                    InetSocketAddress addr = (InetSocketAddress) proxy.address();
                    if (addr != null) {
                        return true; // Proxy is enabled
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // No proxy detected
    }

    public static boolean isProxyEnabled(Context context) {
        return isSystemProxyEnabled() || isNetworkProxyEnabled(context);
    }

}
