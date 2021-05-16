package com.example.trackit;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShowHistory extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String from, to;
    ArrayList<? extends ArrayList<Object>> locations;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        Bundle args = getIntent().getBundleExtra("locations");
        locations = (ArrayList<? extends ArrayList<Object>>) args.getSerializable("ArrayList");
        initializeMap();
        sessionManager = new SessionManager(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        //LatLng sydney = new LatLng(-33.852, 151.211);
        for (int i = 0; i < locations.size(); i++){
            mMap.addMarker(new MarkerOptions()
                    .position((LatLng) locations.get(i).get(0))
                    .title("Time: "+locations.get(i).get(1) + "/Speed: " + locations.get(i).get(2)));
            if (i == 0){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((LatLng) locations.get(i).get(0), 12));
            }
        }
    }

    public void initializeMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(ShowHistory.this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShowHistory.this, LocationHistory.class));
        finish();
    }
}