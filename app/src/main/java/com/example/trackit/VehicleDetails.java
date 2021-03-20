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
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VehicleDetails extends AppCompatActivity {
    Button cont;
    private EditText make, model, type, year, numberPlate, color;
    private ProgressBar loading;
    private static String URL ="https://kiptrack.000webhostapp.com/addVehicle.php";
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
        loading = findViewById(R.id.createAccountLoading);
        builder = new AlertDialog.Builder(VehicleDetails.this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        cont = findViewById(R.id.cont);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateMake() | !validateModel() | !validateYear() | !validateNumberPlate() | validateColor()){
                    return;
                }

                Make = make.getText().toString().trim();
                Model = model.getText().toString().trim();
                Type = type.getText().toString().trim();
                Year = year.getText().toString().trim();
                NumberPlate = numberPlate.getText().toString().trim();
                Color = color.getText().toString().trim();

                RegisterCar(Make, Model, Type, Year, NumberPlate, Color);
                //startActivity(new Intent(VehicleDetails.this, TrackingDeviceDetails.class));
            }
        });
    }

    private boolean validateMake() {
        Make = make.getText().toString().trim();
        if (Make.isEmpty()){
            make.setError("Field cannot be empty");
            return false;
        }
        else{
            make.setError(null);
            return true;
        }
    }
    private boolean validateModel() {
        Model = model.getText().toString().trim();
        if (Model.isEmpty()){
            model.setError("Field cannot be empty");
            return false;
        }
        else{
            model.setError(null);
            return true;
        }
    }
    private boolean validateYear() {
        Year = year.getText().toString().trim();
        if (Year.isEmpty()){
            year.setError("Field cannot be empty");
            return false;
        }
        else{
            year.setError(null);
            return true;
        }
    }
    private boolean validateNumberPlate() {
        NumberPlate = numberPlate.getText().toString().trim();
        if (NumberPlate.isEmpty()){
            numberPlate.setError("Field cannot be empty");
            return false;
        }
        else{
            numberPlate.setError(null);
            return true;
        }
    }
    private boolean validateColor() {
        Color = color.getText().toString().trim();
        if (Color.isEmpty()){
            color.setError("Field cannot be empty");
            return false;
        }
        else{
            color.setError(null);
            return true;
        }
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

    private void RegisterCar(final String make, final String model, final String type, final String year, final String numberPlate, final String color) {
        loading.setVisibility(View.VISIBLE);
        cont.setVisibility(View.GONE);

        if (isOnline()){
            final String[] vehicleID = new String[1];
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("Code");
                        String Message = jsonObject.getString("Message");

                        if(code.equals("1")){
                            vehicleID[0] = jsonObject.getString("vehicleID");
                            Intent myIntent = new Intent(VehicleDetails.this, TrackingDeviceDetails.class);
                            myIntent.putExtra("vehicleID", vehicleID[0]);
                            startActivity(myIntent);
                        }
                        else{
                            builder.setIcon(R.drawable.ic_error);
                            builder.setTitle("Error");
                            builder.setMessage("Could not process request, try again.");
                            builder.setCancelable(true);
                            builder.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(VehicleDetails.this, "Register vehicle error"+e.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        cont.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(VehicleDetails.this, "Register vehicle error 2"+error.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    cont.setVisibility(View.VISIBLE);
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("make", make);
                    params.put("model", model);
                    params.put("year", year);
                    params.put("numberPlate", numberPlate);
                    params.put("color", color);
                    params.put("vehicleOwner", vehicleID[0]);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else {
            builder.setIcon(R.drawable.ic_no_internet);
            builder.setTitle("No Connection!");
            builder.setMessage("Check your internet connection and try again");
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //builder.finish();
                    //startActivity(new Intent(VehicleDetails.this, LogIn.class));
                }
            });
            builder.show();
        }
    }
}