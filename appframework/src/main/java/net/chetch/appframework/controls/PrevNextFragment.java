package net.chetch.appframework.controls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.chetch.appframework.GenericFragment;
import net.chetch.appframework.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class PrevNextFragment extends GenericFragment implements View.OnClickListener{
    View prevButton;
    View nextButton;

    protected int position = 0;

    IPrevNextListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int rid = getLayoutResource("prev_next_horizontal");
        View contentView = inflater.inflate(rid, container, false);

        prevButton = contentView.findViewById(getResourceID("prevButton"));
        prevButton.setOnClickListener(this);

        nextButton = contentView.findViewById(getResourceID("nextButton"));
        nextButton.setOnClickListener(this);

        return contentView;
    }

    public void setListener(IPrevNextListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(view == prevButton){
            if(onPrev()) {
                position--;
            }
        } else if(view == nextButton){
            if(onNext()) {
                position++;
            }
        }
    }

    public int getCurrentPosition(){
        return position;
    }

    protected boolean onPrev(){
        return listener == null ? true : listener.onPrev(position);
    }
    protected boolean onNext(){
        return listener == null ? true : listener.onNext(position);
    }
}
