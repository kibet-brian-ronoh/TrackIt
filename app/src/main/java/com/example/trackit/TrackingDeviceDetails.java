package com.example.trackit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TrackingDeviceDetails extends AppCompatActivity {
    Button done;
    TextView needHelp;
    AlertDialog.Builder builder;
    private EditText IMEI, IMSI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_device_details);

        done = findViewById(R.id.done);
        needHelp = findViewById(R.id.helptrackingdevice);
        builder = new AlertDialog.Builder(TrackingDeviceDetails.this);
        IMEI = findViewById(R.id.imei);
        IMSI = findViewById(R.id.imsi);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Check tracking device");
                builder.setMessage("Is your tracking device up and running? Make sure it is running before proceeding.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(TrackingDeviceDetails.this, LocationMap.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}