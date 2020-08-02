package net.chetch.appframework;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

public abstract class GenericActivity extends ActivityBase {
    private ErrorDialogFragment errorDialog;

    private boolean includeOptionsMenu = false;

    protected Class settingsActivityClass;
    protected Class helpActivityClass;

    //timer stuff
    protected int timerDelay = 30;
    boolean timerStarted = false;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            int nextTimer = onTimer();
            if(nextTimer > 0) {
                timerHandler.postDelayed(this, timerDelay * 1000);
            }
        }
    };

    //proivde a stub to override
    protected int onTimer(){
        return timerDelay;
    }

    protected void startTimer(int timerDelay, int postDelay){
        if(timerStarted)return;
        this.timerDelay = timerDelay;

        timerHandler.postDelayed(timerRunnable, postDelay*1000);
        timerStarted = true;
    }

    protected void startTimer(int timerDelay){
        startTimer(timerDelay, timerDelay);
    }

    protected void stopTimer(){
        timerHandler.removeCallbacks(timerRunnable);
        timerStarted = false;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("AppFramework", "GenericActivity.onCreate");
    }

    protected void includeActionBar(Class settingsClass){
        includeOptionsMenu = true;
        if(settingsClass != null) {
            settingsActivityClass = settingsClass;
        }

        try {
            Toolbar toolbar = (Toolbar) findViewById(getResourceID("actionbar"));
            if(toolbar == null){
                throw new Exception("No action bar with resource ID actionbar found");
            }
            setSupportActionBar(toolbar);
        } catch (Exception e){
            Log.e("GA", "includeActionBar: " + e.getMessage());
        }
    }

    protected void includeActionBar(){
        includeActionBar(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(includeOptionsMenu) {
            try {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(getResourceID("options_menu", "menu"), menu);


                for(int i = 0; i < menu.size(); i++){
                    MenuItem mi = menu.getItem(i);
                    String menuItemID = getResources().getResourceEntryName(mi.getItemId());
                    switch(menuItemID){
                        case "options_menu_settings":
                            mi.setOnMenuItemClickListener(menuItem->{
                                openSettings();
                                return true;
                            });
                            break;

                        case "options_menu_about":
                            mi.setOnMenuItemClickListener(menuItem->{
                                openAbout();
                                return true;
                            });
                            break;

                        case "option_menu_help":
                            mi.setOnMenuItemClickListener(menuItem->{
                                openHelp();
                                return true;
                            });
                            break;
                    }
                }


            } catch (Exception e){
                Log.e("GA", "onCreateOptionsMenu: " + e.getMessage());
            }
        }
        return true;
    }

    public void openSettings(){
        if(settingsActivityClass != null) {
            Intent intent = new Intent(this, settingsActivityClass);
            startActivity(intent);
        }
    }

    public void openHelp(){
        if(helpActivityClass != null) {
            Intent intent = new Intent(this, helpActivityClass);
            startActivity(intent);
        }
    }

    public void openAbout(){
        DialogFragment dialog = new AboutDialogFragment();
        dialog.show(getSupportFragmentManager(), "AboutDialog");
    }

    public void showError(int errorCode, String errorMessage){
        Log.e("GAERROR", errorMessage);
        hideProgress();
        dismissError();

        errorDialog = new ErrorDialogFragment();
        errorDialog.errorType = errorCode;
        errorDialog.errorMessage = errorMessage;

        errorDialog.show(getSupportFragmentManager(), "ErrorDialog");
    }

    public void showError(Throwable t){
        showError(0, t.getMessage());
        errorDialog.throwable = t;
    }

    public void showError(String errorMessage){
        showError(0, errorMessage);
    }

    public boolean isErrorShowing(){
        return errorDialog == null ? false : errorDialog.isShowing();
    }

    public void dismissError(){
        if(errorDialog != null)errorDialog.dismiss();
    }

    public void showProgress(int visibility){
        ProgressBar pb = findViewById(getResourceID("progressBar"));
        if(pb != null){
            pb.setVisibility(visibility);
        }

        TextView tv = findViewById(getResourceID("progressInfo"));
        if(tv != null){
            tv.setVisibility(visibility);
        }
    }

    public void showProgress(){ showProgress(View.VISIBLE); }
    public void hideProgress(){ showProgress(View.INVISIBLE); }

    public void setProgressInfo(String info){
        TextView tv = findViewById(getResourceID("progressInfo"));
        if(tv != null){
            tv.setText(info);
        }
    }

    protected boolean permissionGranted(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    protected void enableTouchEvents(boolean enable){
        if(enable){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    public void showWarningDialog(String warning){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResourceString("dialog.warning.title"));
        alertDialog.setMessage(warning);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResourceString("button.ok"), (dialog,which)->{
                   dialog.dismiss();
                });
        alertDialog.show();
    }

    public void showConfirmationDialog(String message, DialogInterface.OnClickListener okListener){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResourceString("dialog.confirmation.title"));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResourceString("button.ok"), okListener);

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResourceString("button.cancel"), (dialog,which)->{
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    public void onDialogPositiveClick(GenericDialogFragment dialog){

    }

    public void onDialogNegativeClick(GenericDialogFragment dialog){

    }


    protected void enableDeviceWakeup(){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    protected void setWakeUp(Calendar wakeUp){
        if(wakeUp != null &&  wakeUp.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) { //test to see if this is ahead of now
           throw new IllegalArgumentException("Cannot set wakeup to be in the past");
        }

        //create an app wakeup
        Context ctx = getApplicationContext();
        Intent intent = new Intent(ctx, this.getClass());
        intent.putExtra("wakeup", true);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager mgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        mgr.cancel(pi);    // Cancel any previously-scheduled wakeups

        if(wakeUp != null) {
            mgr.set(AlarmManager.RTC_WAKEUP, wakeUp.getTimeInMillis(), pi);
        }
    }

    protected void cancelWakeUp(){
        setWakeUp(null);
    }
}
