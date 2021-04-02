package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class MyTrackingDevice extends AppCompatActivity {
    TextView IMEI, IMSI;
    String Imei, Imsi;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tracking_device);

        Toolbar toolbar = findViewById(R.id.trackingDeviceToolbar);
        toolbar.setTitle("My tracking device");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        IMEI = findViewById(R.id.imeiNo);
        IMSI = findViewById(R.id.imsiNo);

        sessionManager = new SessionManager(this);
        HashMap<String, String> trackingDetails = new HashMap<>();
        trackingDetails = sessionManager.getUserDetail();

        Imei = trackingDetails.get("Imei");
        Imsi = trackingDetails.get("Imsi");

        IMEI.setText(Imei);
        IMSI.setText(Imsi);

    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}