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

abstract public class PrevNextFragment extends GenericFragment implements View.OnClickListener{
    View prevButton;
    View nextButton;

    int position = 0;


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

    @Override
    public void onClick(View view) {
        if(view == prevButton){
            position--;
            onPrev();
        } else if(view == nextButton){
            position++;
            onNext();
        }
    }

    abstract protected void onPrev();
    abstract protected void onNext();
}
