package com.example.trackit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {
    private Button cont;
    private EditText firstName, lastName, email, phoneNo, password, cPassword;
    private static String URL ="https://kiptrack.000webhostapp.com/createAccount.php";
    private ProgressBar loading;
    TextView loginOption;
    AlertDialog.Builder builder;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        cont = findViewById(R.id.continuebtn);
        loginOption = findViewById(R.id.loginoption);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneno);
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.confirmpassword);
        loading = findViewById(R.id.loading);
        sessionManager = new SessionManager(this);
        builder = new AlertDialog.Builder(CreateAccount.this);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mfirstName = firstName.getText().toString().trim();
                String mlastName = lastName.getText().toString().trim();
                String memail = email.getText().toString().trim();
                String mphoneNo = phoneNo.getText().toString().trim();
                String mpassword = password.getText().toString().trim();
                if (!mfirstName.isEmpty() || !mlastName.isEmpty() || !memail.isEmpty() || !mphoneNo.isEmpty() || !mpassword.isEmpty()){
                    Register();
                }
                else {
                        builder.setTitle("Empty fields");
                        builder.setIcon(R.drawable.ic_error);
                        builder.setMessage("Please fill in all the fields.");
                        builder.setCancelable(true);
                        builder.show();
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
    private void Register(){
        loading.setVisibility(View.VISIBLE);
        cont.setVisibility(View.GONE);

        final String firstName = this.firstName.getText().toString().trim();
        final String lastName = this.lastName.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String phoneNo = this.phoneNo.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String ownerID = jsonObject.getString("ownerID");

                    if (code.equals("1")){
                        startActivity(new Intent(CreateAccount.this, CreateAccount2.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateAccount.this, "Register error"+e.toString(), Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    cont.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateAccount.this, "Register error"+error.toString(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                cont.setVisibility(View.VISIBLE);
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
                params.put("phoneNumber", phoneNo);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}