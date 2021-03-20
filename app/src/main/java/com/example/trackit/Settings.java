package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {
    CardView myAccount, myCar, report, trackingDevice, logout, helpInstall;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myAccount = findViewById(R.id.account);
        myCar = findViewById(R.id.mycar);
        report = findViewById(R.id.reportStolen);
        trackingDevice = findViewById(R.id.abouttrackingDevice);
        logout = findViewById(R.id.logout);
        helpInstall = findViewById(R.id.helpInstall);
        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        toolbar.setTitle("More");
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.more);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.current_location:
                        startActivity(new Intent(Settings.this, LocationMap.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.location_history:
                        startActivity(new Intent(Settings.this, LocationHistory.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        return true;
                }
                return false;
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Me.class));
            }
        });
        myCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MyCar.class));
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, ReportStolen.class));
            }
        });
        trackingDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, MyTrackingDevice.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder logoutAlert = new AlertDialog.Builder(Settings.this);
                logoutAlert.setTitle("Logout?");
                logoutAlert.setMessage("Do you want to logout?");
                logoutAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.Logout();
                    }
                });
                logoutAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                logoutAlert.show();
            }
        });
        helpInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, HelpInstallTrackingDevice.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Settings.this, LocationHistory.class));
    }
}