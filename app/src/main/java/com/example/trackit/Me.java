package com.example.trackit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class Me extends AppCompatActivity {
    SessionManager sessionManager;
    TextView phone, email1, email2, nameTxt;
    ImageView changePhone, changeEmail;
    String name, email, phoneNo;
    RelativeLayout changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        phone = findViewById(R.id.car_yeartxt);
        email1 = findViewById(R.id.mail);
        email2 = findViewById(R.id.car_model);
        nameTxt = findViewById(R.id.name);
        changePhone = findViewById(R.id.changephonebtn);
        changeEmail = findViewById(R.id.changeemailbtn);
        changePassword = findViewById(R.id.changePass);

        sessionManager = new SessionManager(this);
        HashMap<String, String> userDetail = new HashMap<>();
        userDetail = sessionManager.getUserDetail();

        name = userDetail.get("NAME");
        email = userDetail.get("EMAIL");
        phoneNo = userDetail.get("phoneNo");

        nameTxt.setText(name);
        phone.setText(phoneNo);
        email1.setText(email);
        email2.setText(email);

        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Me.this);
                final View changePhoneLayout = getLayoutInflater().inflate(R.layout.change_phone, null);
                final EditText newPhone = changePhoneLayout.findViewById(R.id.newNo);
                newPhone.setHint(phoneNo);
                builder.setView(changePhoneLayout);
                builder.setTitle("Change Phone Number");
                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                        awesomeValidation.addValidation(newPhone, RegexTemplate.NOT_EMPTY, "Please enter new number!");
                        if (awesomeValidation.validate()){
                            String newNo = newPhone.getText().toString().trim();
                            if (newNo.equals(phoneNo)){
                                Toast.makeText(Me.this, "Number already existing", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(Me.this, "Sending request....", Toast.LENGTH_SHORT).show();
                                changeNo(newNo, dialog);
                            }
                        }
                        else {
                            Toast.makeText(Me.this, "Cannot have an empty new number", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Me.this);
                final View changeEmailLayout = getLayoutInflater().inflate(R.layout.change_email, null);
                final EditText newEmail = changeEmailLayout.findViewById(R.id.newEmail);
                newEmail.setHint(email);
                builder1.setView(changeEmailLayout);
                builder1.setTitle("Change Email");
                builder1.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                        awesomeValidation.addValidation(newEmail, RegexTemplate.NOT_EMPTY, "Please enter new email!");
                        if (awesomeValidation.validate()){
                            String newMail = newEmail.getText().toString().trim();
                            if (newMail.equals(email)){
                                Toast.makeText(Me.this, "Email already existing", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(Me.this, "Sending request....", Toast.LENGTH_SHORT).show();
                                changeEmail(newMail, dialog);
                            }
                        }
                        else {
                            Toast.makeText(Me.this, "Cannot have an empty Email", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder1.create();
                dialog.show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Me.this);
                final View changePasswordLayout = getLayoutInflater().inflate(R.layout.change_password, null);
                final EditText oldPassword = changePasswordLayout.findViewById(R.id.oldPass);
                final EditText newPassword = changePasswordLayout.findViewById(R.id.newPass);
                final EditText confirmNew = changePasswordLayout.findViewById(R.id.confirmPass);
                builder2.setView(changePasswordLayout);
                builder2.setTitle("Change Password");
                builder2.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
                        awesomeValidation.addValidation(oldPassword, RegexTemplate.NOT_EMPTY, "Please enter old password!");
                        awesomeValidation.addValidation(newPassword, RegexTemplate.NOT_EMPTY, "Please enter new password!");
                        awesomeValidation.addValidation(confirmNew, newPassword.getText().toString(), "Password mismatch!");
                        if (awesomeValidation.validate()){
                            String newPass = newPassword.getText().toString();
                            String oldPass = oldPassword.getText().toString();
                            Toast.makeText(Me.this, "Sending request...", Toast.LENGTH_SHORT).show();
                            changePassword(newPass, oldPass, dialog);
                        }
                        else {
                            Toast.makeText(Me.this, "Incorrect inputs!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder2.create();
                dialog.show();
            }
        });
    }

    public void changeNo(final String newNo, final DialogInterface dialog){
        String URL = "https://kiptrack.000webhostapp.com/changePhoneNumber.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String message = jsonObject.getString("Message");
                    if (code.equals("1")){
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Me.this);
                        builder.setTitle("Successful");
                        builder.setIcon(R.drawable.ic_done);
                        builder.setMessage("Phone number changed successfully");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sessionManager.editUserDetail("phoneNo", newNo);
                                phone.setText(newNo);
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    else {
                        Toast.makeText(Me.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Me.this, "Could not process request" + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Me.this, "Could not process request 2" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("newNo", newNo);
                params.put("ownerID", sessionManager.getUserDetail().get("ownerID"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void changeEmail(final String mail, final DialogInterface dialog){
        String URL = "https://kiptrack.000webhostapp.com/changeEmail.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String Message = jsonObject.getString("Message");
                    if (code.equals("1")){
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Me.this);
                        builder.setTitle("Successful");
                        builder.setIcon(R.drawable.ic_done);
                        builder.setMessage(Message);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sessionManager.editUserDetail("EMAIL", mail);
                                email1.setText(mail);
                                email2.setText(mail);
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    else {
                        Toast.makeText(Me.this, "Could not process request, try again later", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Me.this, "Cannot process request" + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Me.this, "Cannot process request" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerID", sessionManager.getUserDetail().get("ownerID"));
                params.put("newEmail", mail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void changePassword(final String password, final String oldPass, final DialogInterface dialog){
        String URL = "https://kiptrack.000webhostapp.com/changePassword.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("Code");
                    String message =  jsonObject.getString("Message");

                    if (code.equals("1")){
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Me.this);
                        builder.setTitle("Successful");
                        builder.setMessage(message);
                        builder.setIcon(R.drawable.ic_done);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    else if (code.equals("3")){
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Me.this);
                        builder.setTitle("Failed");
                        builder.setMessage(message);
                        builder.setIcon(R.drawable.ic_error1);
                        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    else {
                        Toast.makeText(Me.this, "Could not perform request", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Me.this, "Could not perform request" + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Me.this, "Could not perform request" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("oldPassword", oldPass);
                params.put("newPassword", password);
                params.put("ownerID", sessionManager.getUserDetail().get("ownerID"));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}