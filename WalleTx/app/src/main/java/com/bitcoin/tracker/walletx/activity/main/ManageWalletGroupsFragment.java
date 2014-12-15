package com.bitcoin.tracker.walletx.activity.main;

import android.app.Activity;
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

/**
 * ManageWalletGroupsFragment allows user to perform
 * CRUD operations on wallet groups.
 */
public class ManageWalletGroupsFragment extends Fragment {

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Returns a new instance of this fragment for the given section number.
    public static ManageWalletGroupsFragment newInstance(int sectionNumber) {
        ManageWalletGroupsFragment fragment = new ManageWalletGroupsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ManageWalletGroupsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_manage_wallet_groups, container, false);
        return rootView;
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
        inflater.inflate(R.menu.groups, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_group) {
            Toast.makeText(getActivity(), "TODO: Add New Wallet Group", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

} // ManageWalletGroupsFragment