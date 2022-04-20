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

abstract public class PrevNextFragment extends GenericFragment implements View.OnClickListener{
    View prevButton;
    View nextButton;

    protected int position = 0;

    Observer observer;

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

    protected boolean isValidPosition(int position){
        return true;
    }

    public void observe(Observer observer){
        this.observer = observer;
    }

    @Override
    public void onClick(View view) {
        if(view == prevButton){
            if(isValidPosition(position - 1)) {
                position--;
                onPrev();
            }
        } else if(view == nextButton){
            if(isValidPosition(position + 1)) {
                position++;
                onNext();
            }
        }
    }

    public int getCurrentPosition(){
        return position;
    }

    protected void onPrev(){
        if(observer != null)observer.onChanged(this);
    }
    protected void onNext(){
        if(observer != null)observer.onChanged(this);
    }
}
