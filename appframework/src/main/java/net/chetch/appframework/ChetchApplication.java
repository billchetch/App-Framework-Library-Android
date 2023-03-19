package net.chetch.appframework;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
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
    public String VERSION = "";
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
        Logger.info("Registering uce for " + getClass().getCanonicalName());
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

    }

    public void restartApp(int delayInSecs){
        Intent intent = getPackageManager().getLaunchIntentForPackage( getPackageName() );

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, intent.getFlags());

        AlarmManager mgr = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000*delayInSecs, pendingIntent);

        Logger.info("Restarting app in " + delayInSecs + " seconds");
        System.exit(0);
    }
}
