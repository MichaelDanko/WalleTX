package com.bitcoin.tracker.walletx.activity.navDrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.navDrawer.myWallets.MyWalletsFragment;
import com.bitcoin.tracker.walletx.activity.navDrawer.walletGroups.UpdateWalletGroupActivity;
import com.bitcoin.tracker.walletx.activity.navDrawer.walletGroups.WalletGroupFragment;

/**
 * Handles the display of and interactions with the fragments
 * accessed from within the navigation.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
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
                fragment = MyWalletsFragment.newInstance(position + 1);
                break;
            case 1:
                fragment = WalletGroupFragment.newInstance(position + 1);
                break;
            case 2:
                fragment = ManageTxCategoriesFragment.newInstance(position + 1);
                break;
            case 3:
                fragment = SettingsFragment.newInstance(position + 1);
                break;

            //region TEMPORARY TESTING AREA. DELETE BEFORE LAUNCH.
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
            //endregion

            default:
                fragment = MyWalletsFragment.newInstance(position + 1);
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
                mTitle = getString(R.string.title_my_wallets);
                break;
            case 2:
                mTitle = getString(R.string.title_fragment_wallet_groups);
                break;
            case 3:
                mTitle = getString(R.string.title_tx_catgeories);
                break;
            case 4:
                mTitle = getString(R.string.title_settings);
                break;

            //region TEMPORARY TESTING AREA. DELETE BEFORE LAUNCH.
            case 5:
                mTitle = getString(R.string.testing_carroll);
                break;
            case 6:
                mTitle = getString(R.string.testing_danko);
                break;
            case 7:
                mTitle = getString(R.string.testing_howell);
                break;
            case 8:
                mTitle = getString(R.string.testing_solano);
                break;
            //endregion

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
        int id = item.getItemId();
        // global menu contains no actions
        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(String id) {
        // Handle click events associated with fragments.
    }

} // MainActivity
