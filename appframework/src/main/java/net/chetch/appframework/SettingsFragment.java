package net.chetch.appframework;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        int resource = getResources().getIdentifier("preferences", "xml", getActivity().getPackageName());
        addPreferencesFromResource(resource);
    }
}
