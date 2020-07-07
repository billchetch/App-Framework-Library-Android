package net.chetch.appframework;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import net.chetch.utilities.Logger;
import net.chetch.utilities.UncaughtExceptionHandler;

public class ChetchApplication extends Application {
    protected String LOG_FILE = "chetchapp_log";
    protected UncaughtExceptionHandler uncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialise logger
        Logger.init(this, LOG_FILE);
        //Logger.clear();
        Logger.info("Application started");

        //set default uce handle
        if(uncaughtExceptionHandler == null){
            uncaughtExceptionHandler = new UncaughtExceptionHandler(this, LOG_FILE);
        }
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

    }
}
