package net.chetch.appframework;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ErrorDialogFragment extends GenericDialogFragment implements OnClickListener {

    public int errorType;
    public String errorMessage;
    public Throwable throwable = null;

    public ErrorDialogFragment(){

    }

    public int getErrorType(){ return errorType; }

    public boolean isErrorType(int errorType){
        return this.errorType == errorType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateContentView("dialog_error");

        TextView details = contentView.findViewById(getResourceID("errorDetails"));
        if(errorType > 0) {
            details.setText(errorType + ": " + errorMessage);
        } else {
            details.setText(errorMessage);
        }

        contentView.findViewById(getResourceID("okButton")).setOnClickListener(this);

        // Create the AlertDialog object and return it
        dialog = createDialog();

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
