package com.example.trackit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences =context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void editUserDetail(String prefName, String valueToEdit){
        editor.putString(prefName, valueToEdit);
        editor.apply();
    }
    public void createSession(String name, String email, String ownerID, String phoneNo, String vehicleID, String make, String model, String year, String type, String plateNumber, String color, String trackingDeviceID, String Imei, String Imsi){
        editor.putBoolean("LOGIN", true);
        editor.putString("NAME", name);
        editor.putString("EMAIL", email);
        editor.putString("ownerID", ownerID);
        editor.putString("phoneNo", phoneNo);
        editor.putString("vehicleID", vehicleID);
        editor.putString("make", make);
        editor.putString("model", model);
        editor.putString("year", year);
        editor.putString("type", type);
        editor.putString("plateNumber", plateNumber);
        editor.putString("color", color);
        editor.putString("trackingDeviceID", trackingDeviceID);
        editor.putString("Imei", Imei);
        editor.putString("Imsi", Imsi);

        editor.apply();
    }
    public Boolean isLogin(){
        return sharedPreferences.getBoolean("LOGIN",false);
    }
    public void checkLogin (){
        if (!this.isLogin()){
            Intent i = new Intent(context, LogIn.class);
            context.startActivity(i);
            ((LocationMap) context).finish();
        }
    }
    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put("ownerID", sharedPreferences.getString("ownerID", null));
        user.put("phoneNo", sharedPreferences.getString("phoneNo", null));
        user.put("vehicleID", sharedPreferences.getString("vehicleID", null));
        user.put("make", sharedPreferences.getString("make", null));
        user.put("model", sharedPreferences.getString("model", null));
        user.put("year", sharedPreferences.getString("year", null));
        user.put("type", sharedPreferences.getString("type", null));
        user.put("plateNumber", sharedPreferences.getString("plateNumber", null));
        user.put("color", sharedPreferences.getString("color", null));
        user.put("trackingDeviceID", sharedPreferences.getString("trackingDeviceID", null));
        user.put("Imei", sharedPreferences.getString("Imei", null));
        user.put("Imsi", sharedPreferences.getString("Imsi", null));

        return user;
    }
    public void Logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LogIn.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
        ((Settings) context).finish();
    }
}
