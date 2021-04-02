package com.example.trackit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrackingDeviceDetails extends AppCompatActivity {
    Button done;
    TextView needHelp;
    AlertDialog.Builder builder;
    private ProgressBar loading;
    private EditText IMEI, IMSI;
    private final String URL = "https://kiptrack.000webhostapp.com/register.php";
    private String mIMEI;
    private String mIMSI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_device_details);

        done = findViewById(R.id.done);
        needHelp = findViewById(R.id.helptrackingdevice);
        builder = new AlertDialog.Builder(TrackingDeviceDetails.this);
        IMEI = findViewById(R.id.imei);
        IMSI = findViewById(R.id.imsi);
        loading = findViewById(R.id.TrackingDeviceLoading);

        final String firstName = getIntent().getStringExtra("firstName");
        final String lastName = getIntent().getStringExtra("lastName");
        final String email = getIntent().getStringExtra("email");
        final String phoneNo = getIntent().getStringExtra("phoneNo");
        final String password = getIntent().getStringExtra("password");
        final String make = getIntent().getStringExtra("make");
        final String model = getIntent().getStringExtra("model");
        final String year = getIntent().getStringExtra("year");
        final String type = getIntent().getStringExtra("type");
        final String numberplate = getIntent().getStringExtra("numberPlate");
        final String color = getIntent().getStringExtra("color");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIMEI = IMEI.getText().toString().trim();
                mIMSI = IMSI.getText().toString().trim();

                AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                awesomeValidation.addValidation(IMEI, RegexTemplate.NOT_EMPTY, "Please enter the IMEI of tracker");
                awesomeValidation.addValidation(IMSI, RegexTemplate.NOT_EMPTY, "Please enter the IMSI of tracker");
                if (awesomeValidation.validate()){
                    if (isOnline()) {
                        Register(firstName, lastName, email, phoneNo, password, make, model, year, type, numberplate, color, mIMEI, mIMSI);
                    }
                    else {
                        builder.setMessage("Please check your internet connection");
                        builder.setTitle("No internet!");
                        builder.setIcon(R.drawable.ic_no_internet);
                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                loading.setVisibility(View.GONE);
                                done.setVisibility(View.VISIBLE);
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void Register(final String firstName, final String lastName, final String email, final String phoneNumber, final String password, final String make, final String model, final String year, final String type, final String numberPlate, final String color, final String imei, final String imsi){
        loading.setVisibility(View.VISIBLE);
        done.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Code = jsonObject.getString("Code");
                    String Message = jsonObject.getString("Message");

                    if (Code.equals("1")){

                        builder.setTitle("Success");
                        builder.setMessage(Message);
                        builder.setIcon(R.drawable.ic_done);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(TrackingDeviceDetails.this, LogIn.class));
                            }
                        });
                    }
                    else {
                        loading.setVisibility(View.GONE);
                        done.setVisibility(View.VISIBLE);
                        builder.setIcon(R.drawable.ic_error1);
                        builder.setTitle("Error");
                        builder.setMessage(Message);
                        builder.setCancelable(true);
                    }
                    builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TrackingDeviceDetails.this, "Registration error: "+e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TrackingDeviceDetails.this, "Register error: "+error.toString(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("phoneNo", phoneNumber);
                params.put("password", password);
                params.put("make", make);
                params.put("model", model);
                params.put("year", year);
                params.put("numberPlate", numberPlate);
                params.put("type", type);
                params.put("color", color);
                params.put("imei", imei);
                params.put("imsi", imsi);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}