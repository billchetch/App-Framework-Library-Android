package net.chetch.appframeworklib;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import net.chetch.appframework.GenericDialogFragment;

public class CustomDialogFragment extends GenericDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateContentView("custom_dialog");

        // Create the AlertDialog object and return it
        dialog = createDialog();

        return dialog;
    }
}
