package net.chetch.appframework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import androidx.preference.PreferenceManager;

import net.chetch.utilities.SLog;

abstract public class SettingsActivityBase extends ActivityBase implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String LOG_TAG = "Settings";

    protected ErrorDialogFragment errorDialog;

    protected boolean restartMainActivityOnFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource("activity_settings"));

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);

    }

    protected void onCreatePreferences(SettingsFragment fragment){
        //SLog.i(LOG_TAG, "Create preferences");

    }

    @Override
    protected void onDestroy(){
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(restartMainActivityOnFinish){
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public ErrorDialogFragment showError(int errorCode, String errorMessage, boolean useOkButton){
        if(SLog.LOG)SLog.e("SettingsError", errorMessage);

        if(errorDialog != null){
            errorDialog.dismiss();
        }

        errorDialog = new ErrorDialogFragment();
        errorDialog.errorCode = errorCode;
        errorDialog.errorMessage = errorMessage;
        errorDialog.useOkButton = useOkButton;

        errorDialog.show(getSupportFragmentManager(), "ErrorDialog");
        return errorDialog;
    }
}
