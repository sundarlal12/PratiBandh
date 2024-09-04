package com.vaptlab.pratibandhsdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.BatteryManager;

public class USBdetection {

    private Context context;

    public USBdetection(Context context) {
        this.context = context;
    }

    public boolean isUsbConnected() {
        // Check if connected to a computer via USB
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        boolean connectedToComputer = false;
        if (batteryStatus != null) {
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            connectedToComputer = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        }

        // Check if any USB devices are connected to the phone
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        boolean connectedToUsbDevice = usbManager != null && usbManager.getDeviceList().size() > 0;

        // Return true if connected to either a computer or USB device
        return connectedToComputer || connectedToUsbDevice;
    }
}
