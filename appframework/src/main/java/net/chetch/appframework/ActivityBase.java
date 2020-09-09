package net.chetch.appframework;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

public abstract class ActivityBase extends AppCompatActivity {

    protected int getResourceID(String resourceName){
        return getResources().getIdentifier(resourceName, "id", getPackageName());
    }

    protected int getResourceID(String resourceName, String resourceType){
        return getResources().getIdentifier(resourceName, resourceType, getPackageName());
    }

    protected int getLayoutResource(String resourceName){
        return getResourceID(resourceName, "layout");
    }

    protected int getColorResource(String resourceName){
        int resource = getResourceID(resourceName, "color");
        return ContextCompat.getColor(getApplicationContext(), resource);
    }

    protected Drawable getDrawableResource(String resourceName){
        int resource = getResourceID(resourceName, "drawable");
        return ContextCompat.getDrawable(getApplicationContext(), resource);
    }

    protected int getStringResource(String resourceName){
        return getResourceID(resourceName, "string");
    }

    protected String getResourceString(String resourceName){
        return getString(getStringResource(resourceName));
    }

    protected String getResourceName(int resource){
        return getResources().getResourceEntryName(resource);
    }
}
