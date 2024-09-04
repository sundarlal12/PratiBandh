package com.vaptlab.pratibandh;
import com.vaptlab.pratibandhsdk.SecuritySDK;
import com.vaptlab.pratibandhsdk.EmulatorDetection;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SecuritySDK.initializeAndCheckSecurity(this);
        // Set the content view to the layout defined in content_view.xml
        setContentView(R.layout.activity_main1);

    }


}
