package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MyCar extends AppCompatActivity {
    TextView carMake, carModel, numberPlate, carType, carTitle, color, year;
    String make, model, numberplate, type, Color, Year, title;
    SessionManager sessionManager;
    private Button changeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);
        /*Toolbar toolbar = findViewById(R.id.mycarToolbar);
        toolbar.setTitle("My Car");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        carMake = findViewById(R.id.car_make);
        carModel = findViewById(R.id.car_model);
        numberPlate = findViewById(R.id.plateno);
        carType = findViewById(R.id.car_type1);
        carTitle = findViewById(R.id.car_title);
        color = findViewById(R.id.car_colortxt);
        year = findViewById(R.id.car_yeartxt);
        //changeDetails = findViewById(R.id.edit_car);

        sessionManager = new SessionManager(this);
        HashMap<String, String> carDetails = new HashMap<>();
        carDetails = sessionManager.getUserDetail();

        make = carDetails.get("make");
        model = carDetails.get("model");
        numberplate = carDetails.get("plateNumber");
        type = carDetails.get("type");
        Color = carDetails.get("color");
        Year = carDetails.get("year");
        title = make + " " + model;

        carMake.setText(make);
        carModel.setText(model);
        numberPlate.setText(numberplate);
        color.setText(Color);
        year.setText(Year);
        carTitle.setText(title);
        carType.setText(type);

        //final HashMap<String, String> finalCarDetails = carDetails;
        /*changeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editVehicle(make, model, Year, finalCarDetails.get("type"), Color, numberplate);
            }
        });*/
    }

    public void editVehicle(String make, String model, String year, String type, String color, String numberplate){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Vehicle details");
        final View editVehicleLayout = getLayoutInflater().inflate(R.layout.change_vehicle_details, null);
        final EditText newMake = editVehicleLayout.findViewById(R.id.newMake);
        final EditText newModel = editVehicleLayout.findViewById(R.id.newModel);
        final EditText newYear = editVehicleLayout.findViewById(R.id.newYear);
        final EditText newType = editVehicleLayout.findViewById(R.id.newType);
        final EditText newColor = editVehicleLayout.findViewById(R.id.newColor);
        final EditText newReg = editVehicleLayout.findViewById(R.id.newReg);
        newMake.setText(make);
        newModel.setText(model);
        newYear.setText(year);
        newType.setText(type);
        newColor.setText(color);
        newReg.setText(numberplate);
        builder.setView(editVehicleLayout);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}