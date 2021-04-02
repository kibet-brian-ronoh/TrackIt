package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class ReportStolen extends AppCompatActivity {
    private TextView numberPlate, make, model, year;
    SessionManager sessionManager;
    private String PlateNumber, Make, Model, Year;

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

        sessionManager = new SessionManager(this);
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail = sessionManager.getUserDetail();

        PlateNumber = userDetail.get("plateNumber");
        Make = userDetail.get("make");
        Model = userDetail.get("model");
        Year = userDetail.get("year");

        numberPlate.setText(PlateNumber);
        make.setText(Make);
        model.setText(Model);
        year.setText(Year);
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}