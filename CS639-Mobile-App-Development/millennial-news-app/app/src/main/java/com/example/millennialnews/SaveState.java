package com.example.millennialnews;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveState {

    Context context;
    SharedPreferences sharedPreferences;

    public SaveState(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void setState(boolean bvlaue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("bkey", bvlaue);
        editor.apply();
    }

    public boolean getState() {
        return sharedPreferences.getBoolean("bkey", false);
    }
}
