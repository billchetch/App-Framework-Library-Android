package net.chetch.appframeworklib;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.chetch.appframework.controls.PrevNextFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomPrevNextFragment extends PrevNextFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            return super.onCreateView(inflater, container, savedInstanceState);
        } catch (Exception e){
            Log.e("CPNF", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPrev() {
        Log.i("CPNF", "Previous");
    }

    @Override
    protected void onNext() {
        Log.i("CPNF", "Next");
    }
}
