package com.example.trackit;

import android.widget.EditText;

public class CheckIfEmpty {
    public boolean IsItEmpty(EditText editText){
        String string = editText.getText().toString().trim();
        if (string.isEmpty()){
            editText.setError("Field cannot be blank");
            return false;
        }
        else {
            editText.setError(null);
            return true;
        }
    }
}
