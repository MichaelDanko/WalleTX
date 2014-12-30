package com.bitcoin.tracker.walletx.activity.myWallets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.MainActivity;
import com.bitcoin.tracker.walletx.activity.myWallets.addWallet.AddWalletActivity;

/**
 * MyWalletsFragment acts as the home view for the application.
 * Displays aggregations of wallets.
 */
public class MyWalletsFragment extends Fragment {

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Returns a new instance of this fragment for the given section number.
    public static MyWalletsFragment newInstance(int sectionNumber) {
        MyWalletsFragment fragment = new MyWalletsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MyWalletsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_my_wallets, container, false); // root view
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Add fragment specific action bar items to activity action bar items.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            Toast.makeText(getActivity(), "TODO: Sync data", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_add_wallet) {
            // open new activity
            Intent intent = new Intent( getActivity(), AddWalletActivity.class );
            startActivity( intent );
        }
        return super.onOptionsItemSelected(item);
    }

} // MyWalletsFragment