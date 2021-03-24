package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class CreateAccount extends AppCompatActivity {
    private EditText firstName, lastName, email, phoneNo, password, cPassword;
    TextView loginOption;
    AlertDialog.Builder builder;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button cont = findViewById(R.id.continuebtn);
        loginOption = findViewById(R.id.loginoption);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneno);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.confirmpassword);
        sessionManager = new SessionManager(this);
        builder = new AlertDialog.Builder(CreateAccount.this);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation.addValidation(firstName, "[A-Z][a-z]*", "Please enter a valid name");
                awesomeValidation.addValidation(lastName, "[A-Z][a-z]*", "Please enter a valid name");
                awesomeValidation.addValidation(email, Patterns.EMAIL_ADDRESS, "Invalid Email address");
                awesomeValidation.addValidation(phoneNo, Patterns.PHONE, "Please enter a valid mobile number");
                awesomeValidation.addValidation(password, RegexTemplate.NOT_EMPTY, "Password must be filled");
                awesomeValidation.addValidation(cPassword, password, "Passwords must match");

                if (awesomeValidation.validate()){

                    String mfirstName = firstName.getText().toString().trim();
                    String mlastName = lastName.getText().toString().trim();
                    String memail = email.getText().toString().trim();
                    String mphoneNo = phoneNo.getText().toString().trim();
                    String mpassword = password.getText().toString().trim();

                    Intent intent = new Intent(CreateAccount.this, CreateAccount2.class);
                    intent.putExtra("firstName", mfirstName);
                    intent.putExtra("lastName", mlastName);
                    intent.putExtra("email", memail);
                    intent.putExtra("phoneNo", mphoneNo);
                    intent.putExtra("password", mpassword);

                    startActivity(intent);

                }
            }
        });
        loginOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccount.this, LogIn.class));
            }
        });
    }
}