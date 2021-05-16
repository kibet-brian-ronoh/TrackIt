package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LocationHistory extends AppCompatActivity {
    EditText selectDate;
    ImageView slctDate;
    private int Date, Year, Month;
    String mDate, mMonth, mYear;
    Button showBtn;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    ArrayList<ArrayList<Object>> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);

        selectDate = findViewById(R.id.selectdateEt);
        slctDate = findViewById(R.id.slctdate);
        showBtn = findViewById(R.id.showBtn);
        progressBar = findViewById(R.id.historyLoad);
        sessionManager = new SessionManager(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.location_history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.current_location:
                        startActivity(new Intent(LocationHistory.this, LocationMap.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.location_history:
                        return true;
                    case R.id.more:
                        startActivity(new Intent(LocationHistory.this, Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        slctDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                Date = cal.get(Calendar.DATE);
                Month = cal.get(Calendar.MONTH);
                Year = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LocationHistory.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mMonth = String.format("%02d", (month + 1));
                        mDate = String.format("%02d", dayOfMonth);
                        mYear = String.valueOf(year);
                        selectDate.setText(mYear + "-" + mMonth + "-" + mDate);
                    }
                }, Year, Month, Date);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation.addValidation(selectDate, RegexTemplate.NOT_EMPTY, "Cannot have an empty date!");
                if (awesomeValidation.validate()){
                    String from = (selectDate.getText().toString().trim()) + " 00:00:00";
                    int toDate = Integer.parseInt(mDate);
                    int t = toDate + 1;
                    String to = mYear + "-" + mMonth + "-" + String.format("%02d", t) + " 00:00:00";

                    if (isOnline()){
                        getLocationHistory(from, to);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LocationHistory.this);
                        builder.setTitle("No internet!");
                        builder.setMessage("Check your internet connection and try again.");
                        builder.setIcon(R.drawable.ic_no_internet);
                        builder.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        progressBar.setVisibility(View.GONE);
                        showBtn.setVisibility(View.VISIBLE);
                    }
                    /*Intent intent = new Intent(LocationHistory.this, ShowHistory.class);
                    intent.putExtra("from", from);
                    intent.putExtra("to", to);
                    startActivity(intent);*/
                }
            }
        });
    }

    private void getLocationHistory(final String from, final String to) {
        final String URL = "https://kiptrack.000webhostapp.com/getLocationHistory.php";
        final String trackID = sessionManager.getUserDetail().get("trackingDeviceID");
        progressBar.setVisibility(View.VISIBLE);
        showBtn.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String message = jsonObject.getString("Message");
                    if (code.equals("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++){
                            ArrayList<Object> locationData = new ArrayList<>();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            double latitude = jsonObject1.getDouble("latitude");
                            double longitude = jsonObject1.getDouble("longitude");
                            String time = jsonObject1.getString("time");
                            String speed = jsonObject1.getString("speed");
                            LatLng latLng = new LatLng(latitude, longitude);
                            locationData.add(latLng);
                            locationData.add(time);
                            locationData.add(speed);
                            locations.add(locationData);
                        }
                        Intent intent = new Intent(LocationHistory.this, ShowHistory.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ArrayList", (Serializable)locations);
                        intent.putExtra("locations", args);
                        startActivity(intent);
                    }

                    else if (code.equals("2")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LocationHistory.this);
                        builder.setTitle("No location data");
                        builder.setMessage("There is no location data for this day");
                        builder.setIcon(R.drawable.ic_error1);
                        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        progressBar.setVisibility(View.GONE);
                        showBtn.setVisibility(View.VISIBLE);
                    }
                    else if (code.equals("3")){
                        Toast.makeText(LocationHistory.this, message, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        showBtn.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    showBtn.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LocationHistory.this, "Volley Error"+error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                showBtn.setVisibility(View.VISIBLE);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("from", from);
                params.put("to", to);
                params.put("trackID", trackID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LocationHistory.this, LocationMap.class));
    }
}