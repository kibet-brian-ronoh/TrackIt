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
        sharedPreferences =context.getSharedPreferences("NAME", PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }
    public void createSession(String name, String email){
        editor.putBoolean("LOGIN", true);
        editor.putString("NAME", name);
        editor.putString("EMAIL", email);
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
