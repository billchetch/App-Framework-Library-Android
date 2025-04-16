package net.chetch.appframework;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.chetch.utilities.Logger;


public class AboutDialogFragment extends GenericDialogFragment {

    public String aboutBlurb;
    public String appVersion;

    public AboutDialogFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateContentView("dialog_about");

        //fill in the about stuff
        String version = getAppVersion();
        TextView atv = contentView.findViewById(getResourceID("appVersion"));
        atv.setText(version);

        String blurb = getAboutBlurb();
        setAboutBlurb(blurb);

        //fill in log info
        String logData = Logger.read();
        final TextView ltv = contentView.findViewById(getResourceID("log"));
        if(logData != null) {
            ltv.setText(logData);
        }

        //set clear log button
        Button clearLogButton = contentView.findViewById(getResourceID("clearLogButton"));
        clearLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.clear();
                ltv.setText(Logger.read());
            }
        });

        //set the close button
        Button closeButton = contentView.findViewById(getResourceID("closeButton"));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Create the AlertDialog object and return it
        return createDialog();
    }


    protected String getAppVersion(){
        if(appVersion != null)return appVersion;
        try{
            return "Version: " + getResourceString("app_version");
        } catch (Exception e) {
            return "About version goes here...";
        }

    }
    protected String getAboutBlurb(){
        if(aboutBlurb != null)return aboutBlurb;

        try{
            return getResourceString("about_blurb");
        } catch (Exception e) {
            return "About blurb goes here...";
        }
    }

    public void setAboutBlurb(String blurb){
        if(contentView == null)return;
        aboutBlurb = blurb;
        TextView btv = contentView.findViewById(getResourceID("aboutBlurb"));
        if(btv != null) {
            btv.setText(blurb);
        }
    }
}
