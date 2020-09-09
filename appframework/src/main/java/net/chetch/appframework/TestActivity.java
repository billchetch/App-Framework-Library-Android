package net.chetch.appframework;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

public class TestActivity extends GenericActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource("activity_test"));

        TextView tv = findViewById(getResourceID("packageName"));
        tv.setText(getPackageName());

        Log.i("AppFramework", "TestActivity.onCreate");
    }
}
