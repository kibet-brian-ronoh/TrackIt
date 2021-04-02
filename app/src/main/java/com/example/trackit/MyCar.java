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

public class MyCar extends AppCompatActivity {
    TextView carMake, carModel, numberPlate, color, year;
    String make, model, numberplate, Color, Year;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        Toolbar toolbar = findViewById(R.id.mycarToolbar);
        toolbar.setTitle("My Car");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        carMake = findViewById(R.id.car_make);
        carModel = findViewById(R.id.car_model);
        numberPlate = findViewById(R.id.plateno);
        color = findViewById(R.id.car_colortxt);
        year = findViewById(R.id.car_yeartxt);

        sessionManager = new SessionManager(this);
        HashMap<String, String> carDetails = new HashMap<>();
        carDetails = sessionManager.getUserDetail();

        make = carDetails.get("make");
        model = carDetails.get("model");
        numberplate = carDetails.get("plateNumber");
        Color = carDetails.get("color");
        Year = carDetails.get("year");

        carMake.setText(make);
        carModel.setText(model);
        numberPlate.setText(numberplate);
        color.setText(Color);
        year.setText(Year);
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}