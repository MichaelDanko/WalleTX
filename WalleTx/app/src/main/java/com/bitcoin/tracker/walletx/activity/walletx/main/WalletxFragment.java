package com.bitcoin.tracker.walletx.activity.walletx.main;

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
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.MainActivity;
import com.bitcoin.tracker.walletx.activity.walletx.create.WalletxCreateActivity;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * WalletxFragment acts as the home view for the application.
 * Displays aggregations of wallets.
 */
public class WalletxFragment extends Fragment {

    //region FIELDS

    WalletxExpandableListAdapter mListApapter;
    ExpandableListView mExpListView;
    List<String> mGroupHeader;
    HashMap<String, List<String>> mListDataChild;

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    //endregion

    // Returns a new instance of this fragment for the given section number.
    public static WalletxFragment newInstance(int sectionNumber) {
        WalletxFragment fragment = new WalletxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WalletxFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mExpListView = (ExpandableListView) container.findViewById(android.R.id.list);

        /*---------------------
         * TODO Working here
         * --------------------
         */
        prepareData();

        mListApapter = new WalletxExpandableListAdapter(getActivity(), mGroupHeader, mListDataChild);

        if (mExpListView != null) {
            mExpListView.setAdapter(mListApapter);
        }

        return inflater.inflate(R.layout.fragment_walletx, container, false); // root view
    }

    /*---------------------
     * TODO Working here
     * --------------------
     */
    private void prepareData() {

        mGroupHeader = new ArrayList<>();
        mListDataChild = new HashMap<String, List<String>>();

        List<WalletGroup> groups = WalletGroup.getAllSortedByDisplayOrder();
        for (WalletGroup group : groups) {

            mGroupHeader.add(group.name);

            List<Walletx> wtxs = group.walletxs(); // get all wtxs in this group
            List<String> wtxsInThisGroup = new ArrayList<>(); // names of all wtx in group

            for (Walletx wtx : wtxs) {
                wtxsInThisGroup.add(wtx.name);
            }

            mListDataChild.put(group.name, wtxsInThisGroup);

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    //region OPTIONS MENU

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
            Intent intent = new Intent( getActivity(), WalletxCreateActivity.class );
            startActivity( intent );
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

} // WalletxFragment