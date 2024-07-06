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

public class NotificationBar implements java.util.Observer, View.OnClickListener {
    public interface INotifiable{
        void handleNotification(Object notifier, String tag, Object data);
    }

    public static interface INotificationListener{
        void onClick(NotificationBar nb, NotificationType ntype);

        /*void onShow(NotificationBar nb, NotificationType ntype);

        void onShown(NotificationBar nb, NotificationType ntype);

        void onHide(NotificationBar nb, NotificationType ntype);

        void onHidden(NotificationBar nb, NotificationType ntype);*/
    }

    class Notifier{

        public INotifiable handler;
        public Object notifier;
        public String tag;
        public Object data;

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

        ALERT,

        SUCCESS,

        FAILURE
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
        b.view.setOnClickListener(b);

        String packageName = b.view.getContext().getPackageName();

        b.message = view.findViewById(b.view.getResources().getIdentifier("notificationMessage", "id", packageName));

        int col = b.view.getResources().getIdentifier("bluegreen2", "color", packageName);
        b.colourMap.put(NotificationType.INFO, ContextCompat.getColor(b.view.getContext(), col));

        col = b.view.getResources().getIdentifier("warningYellow", "color", packageName);
        b.colourMap.put(NotificationType.WARNING, ContextCompat.getColor(b.view.getContext(), col));

        col = b.view.getResources().getIdentifier("errorRed", "color", packageName);
        b.colourMap.put(NotificationType.ERROR, ContextCompat.getColor(b.view.getContext(), col));

        col = b.view.getResources().getIdentifier("errorRed", "color", packageName);
        b.colourMap.put(NotificationType.ALERT, ContextCompat.getColor(b.view.getContext(), col));

        col = b.view.getResources().getIdentifier("bluegreen2", "color", packageName);
        b.colourMap.put(NotificationType.SUCCESS, ContextCompat.getColor(b.view.getContext(), col));

        col = b.view.getResources().getIdentifier("warningYellow", "color", packageName);
        b.colourMap.put(NotificationType.FAILURE, ContextCompat.getColor(b.view.getContext(), col));
    }


    static public void setView(View view){
        setView(view, DEFAULT_VIEW_HEIGHT);
    }

    static public NotificationBar monitor(NotificationBar.INotifiable handler, LiveData liveData, String tag){
        NotificationBar b = getInstance();

        b.addSubject(handler, liveData, tag);

        return b;
    }

    static public NotificationBar monitor(NotificationBar.INotifiable handler, Observable observable, String tag){
        NotificationBar b = getInstance();

        b.addSubject(handler, observable, tag);

        return b;
    }

    static public NotificationBar show(NotificationType ntype, String message, Object data, int showFor){
        NotificationBar b = getInstance();

        return b.showNotification(ntype, message, data, showFor);
    }

    static public NotificationBar show(NotificationType ntype, String message, Object data) {
        return show(ntype, message, data, -1);
    }

    static public NotificationBar show(NotificationType ntype, String message) {
        return show(ntype, message, null);
    }

    static public void hide(){
        NotificationBar b = getInstance();

        b.hideNotification();
    }


    //Member stuff here

    View view;
    TextView message;
    int viewHeight  = DEFAULT_VIEW_HEIGHT;
    INotificationListener listener;
    Map<Object, NotificationBar.Notifier> subjects = new HashMap<>();
    Map<NotificationType, Integer> colourMap = new HashMap<>();
    boolean isShowing = false;
    NotificationType notificationTypeShowing;
    Object notificationData;
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

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(this, notificationTypeShowing);
        }
    }

    protected void addSubject(NotificationBar.INotifiable handler, LiveData liveData, String tag){
        if(!subjects.containsKey(liveData)){
            liveData.observeForever( (Object o) -> { update(liveData, o); });
            subjects.put(liveData, new Notifier(handler, liveData, tag));
        }
    }

    protected void addSubject(NotificationBar.INotifiable handler, Observable observable, String tag){
        if(!subjects.containsKey(observable)){
            observable.addObserver(this);
            subjects.put(observable, new Notifier(handler, observable, tag));
        }
    }

    public void setListener(INotificationListener listener){
        this.listener = listener;
    }

    @Override
    public void update(Observable observable, Object o) {
        if(subjects.containsKey(observable)){
            Notifier n = subjects.get(observable);
            n.handler.handleNotification(n.notifier, n.tag, o);
        }
    }

    public void update(LiveData observable, Object o) {
        if(subjects.containsKey(observable)){
            Notifier n = subjects.get(observable);
            n.handler.handleNotification(n.notifier, n.tag, o);
        }
    }


    public NotificationBar showNotification(NotificationType ntype, String message, Object data, int showFor){
        notificationTypeShowing = ntype;
        notificationData = data;
        isShowing = true;
        int col = colourMap.get(ntype);
        view.setBackgroundColor(col);
        this.message.setText(message);
        if(showFor > 0){
            startTimer(showFor);
        } else {
            stopTimer();
        }

        listener = null;
        view.setVisibility(View.VISIBLE);
        //view.setLayoutParams(lp);
        //view.requestLayout();
        ValueAnimator anim = Animation.animateHeight(view, 0, viewHeight, 500);
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                //add here as required
            }
        });
        return this;
    }

    public void hideNotification(){
        isShowing = false;
        listener = null;
        notificationData = null;
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