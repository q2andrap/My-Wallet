package com.dragoninc.mywallet.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Khanh C. Le on 5/17/2016.
 */
public class DataHelper {
    public static DataHelper mInstance;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public static DataHelper getInstance(Context context)
    {
        if (mInstance == null)
            mInstance = new DataHelper(context);
        return mInstance;
    }

    public DataHelper(Context context){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreferences.edit();
    }

    public boolean getBoolean(String key) {
        return this.mPreferences.getBoolean(key, false);
    }

    public void setBoolean(String key, Boolean value)
    {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public String getString(String key) {return this.mPreferences.getString(key, "");}
    
    public void setString(String key, String value){
        mEditor.putString(key, value);
        mEditor.commit();
    }
}
