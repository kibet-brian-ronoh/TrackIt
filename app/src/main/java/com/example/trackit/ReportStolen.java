package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class ReportStolen extends AppCompatActivity {
    private TextView numberPlate, make, model, year, msg;
    SessionManager sessionManager;
    private String PlateNumber, Make, Model, Year, sentence;
    private CardView call;
    private String phoneNumber;
    private Button share;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_stolen);

        Toolbar toolbar = findViewById(R.id.reportToolbar);
        toolbar.setTitle("Report your stolen car");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        numberPlate = findViewById(R.id.numberPlate);
        make = findViewById(R.id.make);
        model = findViewById(R.id.model);
        year = findViewById(R.id.carYear);
        call = findViewById(R.id.call_911);
        share = findViewById(R.id.sharebtn);
        msg = findViewById(R.id.sentence);

        sessionManager = new SessionManager(this);
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail = sessionManager.getUserDetail();

        PlateNumber = userDetail.get("plateNumber");
        Make = userDetail.get("make");
        Model = userDetail.get("model");
        Year = userDetail.get("year");
        phoneNumber = userDetail.get("phoneNo");
        sentence = "If you have seen the above vehicle please contact me on " + phoneNumber +
                " or report to the nearest police station.";

        numberPlate.setText(PlateNumber);
        make.setText(Make);
        model.setText(Model);
        year.setText(Year);
        msg.setText(sentence);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMessage(PlateNumber, Make, Model, Year, phoneNumber);
            }
        });
    }

    public void shareMessage(String PlateNumber, String Make, String Model, String Year, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_SEND);
        String message = "Missing Car Alert!\n" +
                PlateNumber + "\n" +
                Make + " " + Model + "\n" +
                "Year: " + Year + "\n" +
                "If you have seen the above vehicle please contact me on " + phoneNumber +
                " or report to the nearest police station.";
        intent.setType("Text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    public void makePhoneCall(){
        if (ContextCompat.checkSelfPermission(ReportStolen.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ReportStolen.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            //ActivityCompat.requestPermissions(ReportStolen.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
            String dial = "tel:911";
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
            else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}