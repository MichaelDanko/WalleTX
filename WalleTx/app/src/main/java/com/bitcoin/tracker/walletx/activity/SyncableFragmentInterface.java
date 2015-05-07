package com.bitcoin.tracker.walletx.activity;

import android.support.v4.app.Fragment;

/**
 * Interface that allows a SyncableActivity to refresh fragments it contains.
 */
public interface SyncableFragmentInterface {
    public void refreshUi();
}
