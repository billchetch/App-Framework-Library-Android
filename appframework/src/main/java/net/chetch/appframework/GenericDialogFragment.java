package net.chetch.appframework;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class GenericDialogFragment extends AppCompatDialogFragment {

    public enum DisplayOptions{
        NORMAL,
        FULL_SCREEN
    }

    public interface IDismissListener{
        void onDismiss(DialogInterface dialogInterface);
    }

    private IDismissListener dismissListener;

    protected Dialog dialog;
    protected View contentView;
    protected IDialogManager dialogManager;

    protected DisplayOptions displayOptions = DisplayOptions.NORMAL;
    protected double displayScale = 1.0;
    protected int displayMargin = 0;



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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("displayOptions")) {
                displayOptions = DisplayOptions.valueOf(savedInstanceState.getString("displayOptions"));
            }
            if (savedInstanceState.containsKey("displayScale"))
                displayScale = savedInstanceState.getDouble("displayScale");
            if (savedInstanceState.containsKey("displayMargin"))
                displayMargin = savedInstanceState.getInt("displayMargin");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("displayOptions", displayOptions.toString());
        outState.putDouble("displayScale", displayScale);
        outState.putInt("displayMargin", displayMargin);
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

    public void setOnDismissListener(IDismissListener dismissListener){
        this.dismissListener = dismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(this.dismissListener != null){
            dismissListener.onDismiss(dialog);
        }
    }

    public boolean isShowing(){
        return dialog != null ? dialog.isShowing() : false;
    }

    public void setFullScreen(double displayScale, int displayMargin){
        displayOptions = DisplayOptions.FULL_SCREEN;
        this.displayMargin = displayMargin;
        this.displayScale = displayScale;
    }

    public void setFullScreen(double displayScale){
        setFullScreen(displayScale, this.displayMargin);
    }

    protected void redraw(){
        if(dialog != null && displayOptions == DisplayOptions.FULL_SCREEN) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels*displayScale);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*displayScale);
            width -= displayMargin;
            height -= displayMargin;
            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
            lp.width = width;
            lp.height = height;
            contentView.setLayoutParams(lp);
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        redraw();
    }
}
