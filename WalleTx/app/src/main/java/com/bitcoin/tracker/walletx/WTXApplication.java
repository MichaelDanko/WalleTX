package com.bitcoin.tracker.walletx;

import android.app.Application;
import com.activeandroid.ActiveAndroid;

/**
 * A custom application class is required to initialize Active Android.
 *
 * Active Android jar available at https://github.com/pardom/ActiveAndroid/downloads
 */
public class WTXApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

} // WTXApplication
