package com.bitcoin.tracker.walletx;

import android.app.Application;
import com.activeandroid.ActiveAndroid;

/**
 * A custom application class is required to initialize Active Android.
 */
public class WTXApplication extends Application {

    /**
     * Shared preferences file key
     */
    public static final String SP_FILE_KEY = "com.bitcoin.tracker.walletx";

    /**
     * Initializes Active Android
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

}
