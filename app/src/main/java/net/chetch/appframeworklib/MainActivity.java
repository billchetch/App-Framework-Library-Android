package net.chetch.appframeworklib;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.chetch.appframework.GenericActivity;
import net.chetch.appframework.SettingsActivityBase;

public class MainActivity extends GenericActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        includeActionBar(SettingsActivity.class);
    }

    @Override
    public void onClick(View view) {
        String id = getResourceName(view.getId());
        switch(view.getId()){
            case R.id.openErrorDialog:
                showError(1, "This is a test error");
                break;

            case R.id.openAboutDialog:
                openAbout();
                break;

            case R.id.testUCE:
                String x = null;
                x.toLowerCase();
                break;

            default:
                Log.e("Main", "Uncrecognsied " +id);
                break;
        }

        Log.i("Main", "clicked " + id);
    }
}
