package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccount2 extends AppCompatActivity {
    Button addVehicle, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account2);

        addVehicle = findViewById(R.id.addvehicle);
        //skip = findViewById(R.id.skipnow);

        final String firstName = getIntent().getStringExtra("firstName");
        final String lastName = getIntent().getStringExtra("lastName");
        final String email = getIntent().getStringExtra("email");
        final String phoneNo = getIntent().getStringExtra("phoneNo");
        final String password = getIntent().getStringExtra("password");

        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount2.this, VehicleDetails.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("email", email);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("password", password);

                startActivity(intent);

            }
        });
        /*skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccount2.this, LocationMap.class));
            }
        });*/
    }
}