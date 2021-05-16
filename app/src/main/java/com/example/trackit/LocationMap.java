 package com.example.trackit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double myLatitude = -33.865143;
    private Double myLongitude = 151.209900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);
        
        getLocation();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.current_location);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.current_location:
                        return true;
                    case R.id.location_history:
                        startActivity(new Intent(LocationMap.this, LocationHistory.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        startActivity(new Intent(LocationMap.this, Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in current location and move the camera
        LatLng carLocation = new LatLng(myLatitude, myLongitude);
        mMap.addMarker(new MarkerOptions().position(carLocation).title("Lat: " + myLatitude + " Long: " + myLongitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(carLocation, 12));
    }

    //Send a request to the database to get the current location co-ordinates
    public void getLocation(){
        final double[] location = new double[2];
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        final String URL = "https://kiptrack.000webhostapp.com/getLocation.php";
        final String trackID = user.get("trackingDeviceID");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String message = jsonObject.getString("Message");

                    if (code.equals("1")){
                        double latitude = jsonObject.getDouble("latitude");
                        double longitude = jsonObject.getDouble("longitude");
                        myLatitude = latitude;
                        myLongitude = longitude;

                        initializeMap();

                        Toast.makeText(LocationMap.this, message + myLatitude +","+ myLongitude, Toast.LENGTH_SHORT).show();
                    }
                    else if ((code.equals("2"))){
                        Toast.makeText(LocationMap.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else if (code.equals("3")){

                        Toast.makeText(LocationMap.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LocationMap.this, "Could not update location. Try again!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LocationMap.this, "No update on location.Try later" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("trackID", trackID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void initializeMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(LocationMap.this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exit = new AlertDialog.Builder(LocationMap.this);
        exit.setTitle("Exit");
        exit.setMessage("Are you sure you want to exit app?");
        exit.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(LocationMap.this, LogIn.class));
                finish();
            }
        });
        exit.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        exit.show();
    }
}