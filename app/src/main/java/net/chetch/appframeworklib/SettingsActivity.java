package net.chetch.appframeworklib;

import android.content.SharedPreferences;
import android.util.Log;

import net.chetch.appframework.SettingsActivityBase;

public class SettingsActivity extends SettingsActivityBase {


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.i("Main", "settings activity " + s);
    }
}
