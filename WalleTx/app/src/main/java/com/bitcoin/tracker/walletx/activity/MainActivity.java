package com.bitcoin.tracker.walletx.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.txCategories.TxCategoriesFragment;
import com.bitcoin.tracker.walletx.activity.walletx.main.WalletxFragment;
import com.bitcoin.tracker.walletx.activity.settings.SettingsFragment;
import com.bitcoin.tracker.walletx.activity.walletGroup.WalletGroupFragment;

/**
 * Handles the display of and interactions with the fragments
 * accessed from within the navigation.
 */
public class MainActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        WalletGroupFragment.OnFragmentInteractionListener {

    // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;

    // Used to store the last screen title. For use in {@link #restoreActionBar()}.
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Setup the navigation drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    // Updates the main content by replacing fragments
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;

        switch (position) {
            case 0:
                //fragment = SettingsFragment.newInstance(position + 1);
                fragment = WalletxFragment.newInstance(position + 1);
                break;
            case 1:
                //fragment = SettingsFragment.newInstance(position + 1);
                fragment = WalletGroupFragment.newInstance(position + 1);
                break;
            case 2:
                fragment = SettingsFragment.newInstance(position + 1);
                //fragment = TxCategoriesFragment.newInstance(position + 1);
                break;
            case 3:
                fragment = SettingsFragment.newInstance(position + 1);
                break;
            default:
                fragment = SettingsFragment.newInstance(position + 1);
                //fragment = WalletxFragment.newInstance(position + 1);
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    // Updates the action bar title depending upon active fragment.
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.navigation_drawer_title_my_wallets);
                break;
            case 2:
                mTitle = getString(R.string.wallet_group_title_fragment);
                break;
            case 3:
                mTitle = getString(R.string.navigation_drawer_title_tx_catgeories);
                break;
            case 4:
                mTitle = getString(R.string.navigation_drawer_title_settings);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    // Applies the global menu to the action bar when navigation drawer is closed.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    // Handles action bar item clicks associated with the global menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // global menu contains no actions
        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(String id) {
        // Handle click events associated with fragments.
    }

} // MainActivity
