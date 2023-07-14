package net.chetch.appframeworklib;

import android.app.Notification;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.View;

import net.chetch.appframework.GenericActivity;
import net.chetch.appframework.NotificationBar;

import java.util.Calendar;

public class MainActivity extends GenericActivity implements View.OnClickListener {

    CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        includeActionBar(SettingsActivity.class);

        CustomPrevNextFragment prevNextFragment = (CustomPrevNextFragment)getSupportFragmentManager().findFragmentById(R.id.prevNextFragment);
        prevNextFragment.observe(new Observer() {
            @Override
            public void onChanged(Object o) {
                Log.i("Utils", "prev next fragment position ");
            }
        });

        NotificationBar.setView(findViewById(R.id.notificationBar));
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

            case R.id.warningAlert:
                showWarningDialog("This is a test warning");
                break;

            case R.id.confirmationAlert:
                showConfirmationDialog("This is a test conirmation", (dialogInterface, i) -> {
                        Log.i("Main", "Confirmation clicked");
                    });
                break;

            case R.id.testWakeUp:
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(cal.getTimeInMillis() + 2000);
                setWakeUp(cal);
                break;

            case R.id.testCustomDialog:
                if(customDialogFragment != null){
                    customDialogFragment.dismiss();
                }

                customDialogFragment = new CustomDialogFragment();
                customDialogFragment.setFullScreen(0.9);
                customDialogFragment.show(getSupportFragmentManager(), "CustomDialog");
                break;

            case R.id.showNotificationBar:
                NotificationBar nb = NotificationBar.show(NotificationBar.NotificationType.INFO, "Yeip here we go", 5);
                nb.setListener(new NotificationBar.INotificationListener() {

                    @Override
                    public void onClick(NotificationBar nb, NotificationBar.NotificationType ntype) {
                        showError("Wow it worked");
                    }
                });
                break;

            default:
                Log.e("Main", "Uncrecognsied " +id);
                break;
        }

        Log.i("Main", "clicked " + id);
    }
}
