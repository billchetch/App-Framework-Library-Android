package net.chetch.appframework;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        // Load the preferences from an XML resource
        int resource = getResources().getIdentifier("preferences", "xml", getActivity().getPackageName());
        addPreferencesFromResource(resource);

        ((SettingsActivityBase)getActivity()).onCreatePreferences(this);
    }

}
