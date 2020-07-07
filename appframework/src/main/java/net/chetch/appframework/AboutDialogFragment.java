package net.chetch.appframework;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.chetch.utilities.Logger;


public class AboutDialogFragment extends GenericDialogFragment {

    public String aboutBlurb;

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
        String blurb = getAboutBlurb();
        TextView btv = contentView.findViewById(getResourceID("aboutBlurb"));
        btv.setText(blurb);

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

    protected String getAboutBlurb(){
        if(aboutBlurb != null)return aboutBlurb;

        try{
            return getResourceString("about_blurb");
        } catch (Exception e) {
            return "About blurb goes here...";
        }
    }
}
