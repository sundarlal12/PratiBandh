package com.vaptlab.pratibandh;

import com.vaptlab.pratibandhsdk.SecuritySDK;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SecuritySDK securitySDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        String licenseKey = "4435B36A0DFC9977F7F59521";


        securitySDK.initializeAndCheckSecurity(this, licenseKey); // Initialize the SDK with the activity context and license key



    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure that the SDK handles the activity lifecycle, if needed
        if (securitySDK != null) {
            securitySDK.checkActivityLifecycle(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Handle pause logic for the SDK if necessary
        if (securitySDK != null) {
            securitySDK.checkActivityLifecycle(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up any resources the SDK may have allocated
        if (securitySDK != null) {
            securitySDK.checkActivityLifecycle(this);
        }
    }
}
