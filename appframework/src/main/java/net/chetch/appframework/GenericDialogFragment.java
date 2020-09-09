package net.chetch.appframework;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class GenericDialogFragment extends AppCompatDialogFragment {

    protected Dialog dialog;
    protected View contentView;
    protected IDialogManager dialogManager;

    protected int getResourceID(String resourceName){
        return getResources().getIdentifier(resourceName, "id", getContext().getPackageName());
    }

    protected int getResourceID(String resourceName, String resourceType){
        return getResources().getIdentifier(resourceName, resourceType, getContext().getPackageName());
    }

    protected int getLayoutResource(String resourceName){
        return getResourceID(resourceName, "layout");
    }

    protected int getColorResource(String resourceName){
        int resource = getResourceID(resourceName, "color");
        return ContextCompat.getColor(getContext(), resource);
    }

    protected int getStringResource(String resourceName){
        return getResourceID(resourceName, "string");
    }

    protected String getResourceString(String resourceName){
        return getString(getStringResource(resourceName));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof IDialogManager) {
            dialogManager = (IDialogManager) context;
        }
    }

    protected View inflateContentView(String layoutName){
        return inflateContentView(getLayoutResource(layoutName));
    }

    protected View inflateContentView(int layoutResource){
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        contentView = inflater.inflate(layoutResource, null);

        return contentView;
    }

    protected Dialog createDialog(){

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(contentView);

        // Create the Dialog object and return it
        dialog = builder.create();

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;

    }

    public boolean isShowing(){
        return dialog != null ? dialog.isShowing() : false;
    }
}
