package net.chetch.appframework;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import net.chetch.utilities.Animation;

public class NotificationBar implements java.util.Observer {
    public interface INotifiable{
        void handleNotification(Object notifier, String tag);
    }

    class Notifier{

        public INotifiable handler;
        public Object notifier;
        public String tag;

        public Notifier(INotifiable handler, Object notifier, String tag){
            this.handler = handler;
            this.notifier = notifier;
            this.tag = tag;
        }
    }

    public enum NotificationType{
        INFO,
        WARNING,
        ERROR,
    }

    static protected NotificationBar instance;

    static public final int DEFAULT_VIEW_HEIGHT = 120;

    static public NotificationBar getInstance(){
        if(instance == null){
            instance = new NotificationBar();
        }
        return instance;
    }

    static public void setView(View view, int viewHeight){
        NotificationBar b = getInstance();

        b.viewHeight = viewHeight;
        b.view = view;
        b.view.setVisibility(View.GONE);

        String packageName = b.view.getContext().getPackageName();

        b.message = view.findViewById(b.view.getResources().getIdentifier("notificationMessage", "id", packageName));

        int col = b.view.getResources().getIdentifier("bluegreen", "color", packageName);
        b.colourMap.put(NotificationType.INFO, ContextCompat.getColor(b.view.getContext(), col));

        col = b.view.getResources().getIdentifier("warningYellow", "color", packageName);
        b.colourMap.put(NotificationType.WARNING, ContextCompat.getColor(b.view.getContext(), col));

        col = b.view.getResources().getIdentifier("errorRed", "color", packageName);
        b.colourMap.put(NotificationType.ERROR, ContextCompat.getColor(b.view.getContext(), col));
    }


    static public void setView(View view){
        setView(view, DEFAULT_VIEW_HEIGHT);
    }

    static public void monitor(NotificationBar.INotifiable handler, LiveData liveData, String tag){
        NotificationBar b = getInstance();

        b.addSubject(handler, liveData, tag);
    }

    static public void monitor(NotificationBar.INotifiable handler, Observable observable, String tag){
        NotificationBar b = getInstance();

        b.addSubject(handler, observable, tag);
    }

    static public void show(NotificationType ntype, String message, int showFor){
        NotificationBar b = getInstance();

        b.showNotification(ntype, message, showFor);
    }

    static public void show(NotificationType ntype, String message) {
        show(ntype, message, -1);
    }

    static public void hide(){
        NotificationBar b = getInstance();

        b.hideNotification();
    }


    //Member stuff here

    View view;
    TextView message;
    int viewHeight  = DEFAULT_VIEW_HEIGHT;
    Map<Object, NotificationBar.Notifier> subjects = new HashMap<>();
    Map<NotificationType, Integer> colourMap = new HashMap<>();
    boolean isShowing = false;
    int showDuration;

    final int timerDelay = 1; //in seconds
    Calendar timerStartedOn;
    boolean timerStarted = false;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            int nextTimer = onTimer();
            if(nextTimer > 0) {
                timerHandler.postDelayed(this, timerDelay * 1000);
            }
        }
    };

    protected void startTimer(int showFor){
        showDuration = showFor;
        timerStartedOn = Calendar.getInstance();
        if(timerStarted)return;

        timerHandler.postDelayed(timerRunnable, timerDelay*1000);
        timerStarted = true;
    }

    protected void stopTimer(){
        timerHandler.removeCallbacks(timerRunnable);
        timerStarted = false;
    }

    protected int onTimer(){
        int nt = timerDelay;
        long duration = Calendar.getInstance().getTimeInMillis() - timerStartedOn.getTimeInMillis();
        if(isShowing && duration > showDuration*1000){
            hideNotification();
        }

        return nt;
    }

    protected void addSubject(NotificationBar.INotifiable handler, LiveData liveData, String tag){
        if(!subjects.containsKey(liveData)){
            //liveData.observeForever(this);
            //subjects.put(liveData, new Notifier(handler, liveData, tag));
        }
    }

    protected void addSubject(NotificationBar.INotifiable handler, Observable observable, String tag){
        if(!subjects.containsKey(observable)){
            observable.addObserver(this);
            subjects.put(observable, new Notifier(handler, observable, tag));
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if(subjects.containsKey(observable)){
            Notifier n = subjects.get(observable);
            n.handler.handleNotification(n.notifier, n.tag);
        }
    }

    public void onChanged(Object o) {
        if(subjects.containsKey(o)){
            Notifier n = subjects.get(o);
            n.handler.handleNotification(n.notifier, n.tag);
        }
    }


    public void showNotification(NotificationType ntype, String message, int showFor){
        isShowing = true;
        int col = colourMap.get(ntype);
        view.setBackgroundColor(col);
        this.message.setText(message);
        if(showFor > 0){
            startTimer(showFor);
        } else {
            stopTimer();
        }

        view.setVisibility(View.VISIBLE);
        //view.setLayoutParams(lp);
        //view.requestLayout();
        Animation.animateHeight(view, 0, viewHeight, 500);
    }

    public void hideNotification(){
        isShowing = false;
        stopTimer();
        ValueAnimator anim = Animation.animateHeight(view, viewHeight, 0, 500);
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                view.setVisibility(View.GONE);
            }
        });
    }
}