package net.chetch.appframeworklib;

import net.chetch.appframework.ChetchApplication;

public class AppFrameworkApplication extends ChetchApplication {

    @Override
    public void onCreate() {
        LOG_FILE = "apfapp";

        super.onCreate();
    }
}
