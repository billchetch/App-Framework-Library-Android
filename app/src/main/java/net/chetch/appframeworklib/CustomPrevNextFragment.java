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

    @Override
    protected void onPrev() {
        super.onPrev();
        Log.i("CPNF", "Previous");
    }

    @Override
    protected void onNext() {
        super.onNext();
        Log.i("CPNF", "Next");
    }
}
