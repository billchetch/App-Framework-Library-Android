package net.chetch.appframework.controls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.chetch.appframework.GenericFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExpandIconFragment extends GenericFragment implements View.OnClickListener{

    ImageView expandIcon;
    boolean enabled = true;
    boolean expanded = false;
    IExpandIconListener listener;

    public void setListener(IExpandIconListener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int rid = getLayoutResource("expand_icon");
        View contentView = inflater.inflate(rid, container, false);

        expanded = false;
        expandIcon = contentView.findViewById(getResourceID("expandIcon"));
        expandIcon.setOnClickListener(this);


        return contentView;
    }

    @Override
    public void onClick(View view) {
        if(!enabled)return;

        if(expanded){
            contract();
        } else {
            expand();
        }
    }

    public boolean isExpanded(){
        return expanded;
    }

    public void contract(){
        if(!enabled)return;

        if(expanded){
            expandIcon.setRotation(0.0f);
            expanded = false;
            if(listener != null)listener.onContract();
        }
    }

    public void expand(){
        if(!enabled)return;

        if(!expanded){
            expandIcon.setRotation(180.0f);
            expanded = true;
            if(listener != null)listener.onExpand();
        }
    }

    public void disable(){
        contract();
        enabled = false;
        expandIcon.setAlpha(0.5f);;
    }

    public void enable(){
        enabled = true;
        expandIcon.setAlpha(1.0f);;
    }
}
