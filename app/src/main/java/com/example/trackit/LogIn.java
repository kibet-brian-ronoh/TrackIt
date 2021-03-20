package com.example.trackit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {
    private Button login;
    private EditText email, password;
    AlertDialog.Builder builder;
    private ProgressBar progressBar;
    private String Url = "https://kiptrack.000webhostapp.com/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        login = findViewById(R.id.login);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.passwordlogin);
        sessionManager = new SessionManager(this);
        progressBar = findViewById(R.id.login_loading);
        builder = new AlertDialog.Builder(LogIn.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = email.getText().toString().trim();
                String mpassword = password.getText().toString().trim();
                if (!memail.isEmpty() || !mpassword.isEmpty()){
                    LoginFunction(memail, mpassword);
                }
                else {
                    builder.setIcon(R.drawable.ic_error);
                    builder.setTitle("Empty fields!");
                    builder.setMessage("Please fill in all the fields required");
                    builder.setCancelable(true);
                    builder.show();
                }
            }
        });
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

    private void LoginFunction(final String email, final String password) {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(LogIn.this);
        progressBar.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);

        if (isOnline()){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("Code");
                        String message = jsonObject.getString("Message");

                        if (code.equals("1")){
                            String fname = jsonObject.getString("firstName");
                            String email = jsonObject.getString("email");
                            sessionManager.createSession(fname, email);
                            startActivity(new Intent(LogIn.this, LocationMap.class));
                            finish();
                        }
                        else if (code.equals("2")){
                            builder1.setIcon(R.drawable.ic_error);
                            builder1.setTitle(message);
                            builder1.setMessage("Try again");
                            builder1.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    progressBar.setVisibility(View.GONE);
                                    login.setVisibility(View.VISIBLE);
                                }
                            });
                            builder1.show();
                        }
                        else if (code.equals("3")){
                            builder1.setIcon(R.drawable.ic_error);
                            builder1.setTitle(message);
                            builder1.setMessage("Try again");
                            builder1.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    progressBar.setVisibility(View.GONE);
                                    login.setVisibility(View.VISIBLE);
                                }
                            });
                            builder1.show();
                        }
                        else if(code.equals("4")){
                            builder1.setIcon(R.drawable.ic_error);
                            builder1.setTitle(message);
                            builder1.setMessage("Try again");
                            builder1.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    progressBar.setVisibility(View.GONE);
                                    login.setVisibility(View.VISIBLE);
                                }
                            });
                            builder1.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LogIn.this, "Login error"+e.toString(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LogIn.this, "Could not Login"+error.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            builder1.setIcon(R.drawable.ic_no_internet);
            builder1.setTitle("No Connection!");
            builder1.setMessage("Check your internet connection and try again");
            builder1.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder1.show();
            progressBar.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }
}