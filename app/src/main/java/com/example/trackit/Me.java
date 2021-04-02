package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class Me extends AppCompatActivity {
    SessionManager sessionManager;
    TextView phone, email1, email2, nameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        phone = findViewById(R.id.no2);
        email1 = findViewById(R.id.mail);
        email2 = findViewById(R.id.emailtxt2);
        nameTxt = findViewById(R.id.name);

        sessionManager = new SessionManager(this);
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail = sessionManager.getUserDetail();
        String name = userDetail.get("NAME");
        String email = userDetail.get("EMAIL");
        String phoneNo = userDetail.get("phoneNo");
        nameTxt.setText(name);
        phone.setText(phoneNo);
        email1.setText(email);
        email2.setText(email);
    }
}