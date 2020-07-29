package com.anahitavakoli.ketabjooyan.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedPreferencesHandler {

    private static Context context;

    private SharedPreferencesHandler() {
    }

    private static SharedPreferencesHandler spf = new SharedPreferencesHandler();

    public static SharedPreferencesHandler getSpf(Context context) {
        spf.context = context;
        return spf;
    }

    public void saveInfoData(String fileName, Map<String,String> data){

        SharedPreferences spf = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor e = spf.edit();

        for (Map.Entry<String,String> item : data.entrySet()){
            e.putString(item.getKey(),item.getValue());
        }

        e.apply();
    }
}
