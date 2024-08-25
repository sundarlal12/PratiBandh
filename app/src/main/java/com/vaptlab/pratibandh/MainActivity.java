package com.vaptlab.pratibandh;
import com.vaptlab.pratibandhsdk.SecuritySDK;
import com.vaptlab.pratibandhsdk.*;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SecuritySDK.initializeAndCheckSecurity(this);
     //   SignatureUtils.initialize(this);

//        SecuritySDK.checkAppSignatureAndSendMetrics(this);

      //  testRootDetection();
//        testUsbDebuggingDetection();
//        testDeveloperOptionsDetection();

    }
//    public void testRootDetection() {
//        if (MockLocationDetection.isMockLocationEnabled(this)) {
//            //Log.d("DetectionTest", "Root detection detected!");
//            Toast.makeText(this, "mock  detection detected!"+MockLocationDetection.isMockLocationEnabled(this), Toast.LENGTH_LONG).show();
//        } else {
//         //   Log.d("DetectionTest", "Root detection not detected.");
//            Toast.makeText(this, "mock detection not detected.", Toast.LENGTH_LONG).show();
//        }
//    }

//    public void testUsbDebuggingDetection() {
//        if (UsbDebuggingDetection.isUsbDebuggingEnabled(this)) {
//            Log.d("DetectionTest", "USB debugging detected!");
//            Toast.makeText(this, "USB debugging detected!", Toast.LENGTH_LONG).show();
//        } else {
//            Log.d("DetectionTest", "USB debugging not detected.");
//            Toast.makeText(this, "USB debugging not detected.", Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//
//    public void testDeveloperOptionsDetection() {
//        if (DeveloperOptionsDetection.isDeveloperOptionsEnabled(this)) {
//            Log.d("DetectionTest", "Developer options enabled!");
//            Toast.makeText(this, "Developer options enabled!", Toast.LENGTH_LONG).show();
//        } else {
//            Log.d("DetectionTest", "Developer options not enabled.");
//            Toast.makeText(this, "Developer options not enabled.", Toast.LENGTH_LONG).show();
//        }
//    }



}