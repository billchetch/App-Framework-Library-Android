package net.chetch.appframework;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.chetch.utilities.UncaughtExceptionHandler;

//import net.chetch.utilities.UncaughtExceptionHandler;

public class UCEActivity extends GenericActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(getLayoutResource("activity_uce"));
        
        String report = getIntent().getStringExtra(UncaughtExceptionHandler.REPORT);

        Button closeButton = findViewById(getResourceID("uceCloseButton"));
        closeButton.setEnabled(false);
        closeButton.setOnClickListener(this);

        TextView tv = findViewById(getResourceID("uceErrorReport"));
        tv.setText(report);

        startTimer(10);
    }

    @Override
    protected int onTimer(){
        try {
            Button closeButton = findViewById(getResourceID("uceCloseButton"));
            closeButton.setEnabled(true);
        } catch (Exception e){
            //allow to fall silent
        }
        return super.onTimer();
    }

    @Override
    public void onClick(View v) {
        finish();
        System.exit(0);
    }
}
