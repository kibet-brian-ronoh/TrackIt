package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class VehicleDetails extends AppCompatActivity {
    Button cont;
    private EditText make, model, type, year, numberPlate, color;
    private String Make, Model, Type, Year, NumberPlate, Color;
    AlertDialog.Builder builder;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        make = findViewById(R.id.car_make);
        model = findViewById(R.id.car_model);
        type = findViewById(R.id.car_type);
        year = findViewById(R.id.year);
        numberPlate = findViewById(R.id.platenumber);
        color = findViewById(R.id.color);
        builder = new AlertDialog.Builder(VehicleDetails.this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        cont = findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Make = make.getText().toString().trim();
                Model = model.getText().toString().trim();
                Type = type.getText().toString().trim();
                Year = year.getText().toString().trim();
                NumberPlate = numberPlate.getText().toString().trim();
                Color = color.getText().toString().trim();

                final String firstName = getIntent().getStringExtra("firstName");
                final String lastName = getIntent().getStringExtra("lastName");
                final String email = getIntent().getStringExtra("email");
                final String phoneNo = getIntent().getStringExtra("phoneNo");
                final String password = getIntent().getStringExtra("password");

                AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation.addValidation(make, RegexTemplate.NOT_EMPTY, "Field Cannot be empty");
                awesomeValidation.addValidation(model, RegexTemplate.NOT_EMPTY, "Field Cannot be empty");
                awesomeValidation.addValidation(type, RegexTemplate.NOT_EMPTY, "Field Cannot be empty");
                awesomeValidation.addValidation(numberPlate, RegexTemplate.NOT_EMPTY, "Field Cannot be empty");
                awesomeValidation.addValidation(color, RegexTemplate.NOT_EMPTY, "Field Cannot be empty");
                awesomeValidation.addValidation(year, RegexTemplate.NOT_EMPTY, "Field Cannot be empty");

                if (awesomeValidation.validate()){

                    Intent intent = new Intent(VehicleDetails.this, TrackingDeviceDetails.class);
                    intent.putExtra("firstName", firstName);
                    intent.putExtra("lastName", lastName);
                    intent.putExtra("email", email);
                    intent.putExtra("phoneNo", phoneNo);
                    intent.putExtra("password", password);
                    intent.putExtra("make", Make);
                    intent.putExtra("model", Model);
                    intent.putExtra("type", Type);
                    intent.putExtra("year", Year);
                    intent.putExtra("numberPlate", NumberPlate);
                    intent.putExtra("color", Color);

                    startActivity(intent);
                }
            }
        });
    }
}