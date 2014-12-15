package com.bitcoin.tracker.walletx.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.api.BlockchainInfo;
import com.bitcoin.tracker.walletx.ui.fragment.ManageTxCategoriesFragment;
import com.bitcoin.tracker.walletx.ui.fragment.ManageWalletGroupsFragment;
import com.bitcoin.tracker.walletx.ui.fragment.MyWalletsFragment;
import com.bitcoin.tracker.walletx.ui.fragment.NavigationDrawerFragment;
import com.bitcoin.tracker.walletx.ui.fragment.SettingsFragment;
import com.bitcoin.tracker.walletx.ui.fragment.TestingCarrollFragment;
import com.bitcoin.tracker.walletx.ui.fragment.TestingDankoFragment;
import com.bitcoin.tracker.walletx.ui.fragment.TestingHowellFragment;
import com.bitcoin.tracker.walletx.ui.fragment.TestingSolanoFragment;

/**
 * Manages the navigation drawer and displays its associated fragments.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    BlockchainInfo Wallet;

    /**
     * Sets up the navigation drawer.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /**
     * Updates the main content by replacing fragments
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = MyWalletsFragment.newInstance(position + 1);
                break;
            case 1:
                fragment = ManageWalletGroupsFragment.newInstance(position + 1);
                break;
            case 2:
                fragment = ManageTxCategoriesFragment.newInstance(position + 1);
                break;
            case 3:
                fragment = SettingsFragment.newInstance(position + 1);
                break;

            // TEMP: BEGIN TESTING CODE -------------------------------------------------
            case 4:
                fragment = TestingCarrollFragment.newInstance(position + 1);
                break;
            case 5:
                fragment = TestingDankoFragment.newInstance(position + 1);
                break;
            case 6:
                fragment = TestingHowellFragment.newInstance(position + 1);
                break;
            case 7:
                fragment = TestingSolanoFragment.newInstance(position + 1);
                break;
            // TEMP: End testing fragments --------

            default:
                fragment = MyWalletsFragment.newInstance(position + 1);
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    /**
     * Updates the ActionBar title depending upon active fragment.
     * @param number
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_my_wallets);
                break;
            case 2:
                mTitle = getString(R.string.title_manage_wallet_groups);
                break;
            case 3:
                mTitle = getString(R.string.title_manage_tx_catgeories);
                break;
            case 4:
                mTitle = getString(R.string.title_settings);
                break;

            // TEMP: BEGIN TESTING CODE -------------------------------------------------
            case 5:
                mTitle = getString(R.string.dc);
                break;
            case 6:
                mTitle = getString(R.string.md);
                break;
            case 7:
                mTitle = getString(R.string.bh);
                break;
            case 8:
                mTitle = getString(R.string.as);
                break;
            // TEMP: End testing fragments --------

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

} // MainActivity

