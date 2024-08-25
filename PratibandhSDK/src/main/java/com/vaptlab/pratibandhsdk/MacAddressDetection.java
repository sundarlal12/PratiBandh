import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.content.Context;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
public class MacAddressDetection {

    public static String getMacAddress(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // On Android 6.0 and above, MAC address is restricted
            return getAlternativeIdentifier(context);
        } else {
            // For versions below Android 6.0
            return getMacAddressPreM(context);
        }
    }

    // For Android versions prior to 6.0 (API level 23)
    private static String getMacAddressPreM(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    // Alternative identifier for Android 6.0 and above
    private static String getAlternativeIdentifier(Context context) {
        // Get Android ID as a fallback identifier
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        // Optionally, you can include other identifiers such as IMEI or serial number
        String imei = getIMEI(context);
        String serialNumber = getSerialNumber();

        // Combine these identifiers or choose the most suitable one
        return "AndroidID: " + androidId + ", IMEI: " + imei + ", Serial: " + serialNumber;
    }

    // Method to get IMEI (requires permission on Android)
    private static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return telephonyManager.getImei();
            } else {
                return telephonyManager.getDeviceId(); // Deprecated but still used for legacy support
            }
        }
        return "Unavailable";
    }

    // Method to get Serial number (API level 26 and above)
    private static String getSerialNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL; // Deprecated in newer versions
        }
    }
}
