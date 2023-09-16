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
import net.chetch.appframework.controls.ExpandIconFragment;
import net.chetch.appframework.controls.IExpandIconListener;
import net.chetch.appframework.controls.IPrevNextListener;
import net.chetch.appframework.controls.PrevNextFragment;

import java.util.Calendar;

public class MainActivity extends GenericActivity implements View.OnClickListener, IPrevNextListener, IExpandIconListener {

    CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        includeActionBar(SettingsActivity.class);

        PrevNextFragment prevNextFragment = (PrevNextFragment)getSupportFragmentManager().findFragmentById(R.id.prevNextFragment);
        prevNextFragment.setListener(this);

        ExpandIconFragment expandFragment = (ExpandIconFragment)getSupportFragmentManager().findFragmentById(R.id.expandIconFragment);
        expandFragment.setListener(this);

        NotificationBar.setView(findViewById(R.id.notificationBar));
    }

    @Override
    public void onClick(View view) {
        String id = getResourceName(view.getId());

        int viewId = view.getId();
        if (viewId == R.id.openErrorDialog) {
            showError(1, "This is a test error");
        } else if (viewId == R.id.openAboutDialog) {
            openAbout();
        } else if (viewId == R.id.testUCE) {
            String x = null;
            x.toLowerCase();
        } else if (viewId == R.id.warningAlert) {
            showWarningDialog("This is a test warning");
        } else if (viewId == R.id.confirmationAlert) {
            showConfirmationDialog("This is a test conirmation", (dialogInterface, i) -> {
                Log.i("Main", "Confirmation clicked");
            });
        } else if (viewId == R.id.testWakeUp) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(cal.getTimeInMillis() + 2000);
            setWakeUp(cal);
        } else if (viewId == R.id.testCustomDialog) {
            if (customDialogFragment != null) {
                customDialogFragment.dismiss();
            }

            customDialogFragment = new CustomDialogFragment();
            customDialogFragment.setFullScreen(0.9);
            customDialogFragment.show(getSupportFragmentManager(), "CustomDialog");
        } else if (viewId == R.id.showNotificationBar) {
            NotificationBar nb = NotificationBar.show(NotificationBar.NotificationType.INFO, "Yeip here we go", 5);
            nb.setListener(new NotificationBar.INotificationListener() {

                @Override
                public void onClick(NotificationBar nb, NotificationBar.NotificationType ntype) {
                    showError("Wow it worked");
                }
            });
        } else {
            Log.e("Main", "Uncrecognsied " + id);
        }

        Log.i("Main", "clicked " + id);
    }

    @Override
    public boolean onPrev(int position) {
        Log.i("Main", "Prev clicked");
        return true;
    }

    @Override
    public boolean onNext(int position) {
        Log.i("Main", "Next clicked");
        return true;
    }

    @Override
    public void onExpand() {
        Log.i("Main", "Expanded");
    }

    @Override
    public void onContract() {
        Log.i("Main", "Contracted");
    }
}
