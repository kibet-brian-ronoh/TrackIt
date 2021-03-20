package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MyTrackingDevice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tracking_device);

        Toolbar toolbar = findViewById(R.id.trackingDeviceToolbar);
        toolbar.setTitle("My tracking device");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}