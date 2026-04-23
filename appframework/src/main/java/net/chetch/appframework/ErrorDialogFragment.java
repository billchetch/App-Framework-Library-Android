package net.chetch.appframework;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ErrorDialogFragment extends GenericDialogFragment implements OnClickListener {

    public int errorCode;

    public boolean showErrorCode = false;
    public String errorMessage;
    public Throwable throwable = null;

    public boolean useOkButton = true;

    public ErrorDialogFragment(){

    }

    public int getErrorCode(){ return errorCode; }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateContentView("dialog_error");

        TextView details = contentView.findViewById(getResourceID("errorDetails"));
        if(errorCode > 0 && showErrorCode) {
            details.setText(errorCode + ": " + errorMessage);
        } else {
            details.setText(errorMessage);
        }

        Button okButton = contentView.findViewById(getResourceID(("okButton")));
        if(okButton != null){
            if(useOkButton) {
                okButton.setOnClickListener(this);
                okButton.setVisibility(View.VISIBLE);
            } else {
                okButton.setVisibility(View.GONE);
            }
        }

        // Create the AlertDialog object and return it
        dialog = createDialog();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onClick(View v){
        dismiss();

        if(dialogManager != null) {
            dialogManager.onDialogPositiveClick(this);
        }
    }


}
