package net.chetch.appframework;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class GenericFragment extends Fragment {

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
}
