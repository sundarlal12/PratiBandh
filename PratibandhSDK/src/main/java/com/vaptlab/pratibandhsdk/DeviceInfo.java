package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class DeviceInfo {

    private Context context;

    public DeviceInfo(Context context) {
        this.context = context;
    }

    // Get Battery Information as JSON String
    public String getBatteryInfo() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        if (batteryStatus == null) {
            return "{}"; // Return empty JSON object if batteryStatus is null
        }

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale * 100;

        JSONObject batteryInfoJson = new JSONObject();
        try {
            batteryInfoJson.put("batteryLevel", batteryPct + "%");
            batteryInfoJson.put("isCharging", isCharging);
        } catch (JSONException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return batteryInfoJson.toString();
    }

    // Get Carrier Information as JSON String
    public String getCarrierInfo() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        JSONObject carrierInfoJson = new JSONObject();
        try {
            carrierInfoJson.put("carrier", tm.getNetworkOperatorName());
        } catch (JSONException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
        return carrierInfoJson.toString();
    }


    public String getIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<java.net.InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (java.net.InetAddress address : addresses) {
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

//    public String getMacAddress() {
//        try {
//            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            return wifiInfo.getMacAddress();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "Unknown";
//    }

    // Get Wi-Fi Information as JSON String
    public String getWifiInfo() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo == null) {
            return "{}"; // Return empty JSON object if wifiInfo is null
        }

        JSONObject wifiInfoJson = new JSONObject();
        try {
            wifiInfoJson.put("wifiSSID", wifiInfo.getSSID());
            wifiInfoJson.put("signalStrength", wifiInfo.getRssi() + " dBm");
        } catch (JSONException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return wifiInfoJson.toString();
    }
}
