package com.bitcoin.tracker.walletx.activity.txCategories;

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
import com.bitcoin.tracker.walletx.activity.walletGroup.create.WalletGroupCreateActivity;

/**
 * TxCategoriesFragment allows user to perform
 * CRUD operations on tx categories.
 *
 * TODO @as populate the list view from fragment_txcategory.xml
 * TODO @as after new tx cat has been added, repopulate list view to display
 * TODO @as bind click event to action bar menu / list items
 *          that open either create or update activity
 *
 *
 */
public class TxCategoriesFragment extends Fragment {

    private static final int NEW_TAG_ADDED = 1;

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Returns a new instance of this fragment for the given section number.
    public static TxCategoriesFragment newInstance(int sectionNumber) {
        TxCategoriesFragment fragment = new TxCategoriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TxCategoriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_txcategory, container, false);
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
        inflater.inflate(R.menu.categories, menu);
    }

    /**
     * Handles action bar menu interactions for this fragment.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_tx_category) {
            Intent intent = new Intent( getActivity(), TxCategoryCreateActivity.class );
            startActivityForResult( intent, NEW_TAG_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

} // TxCategoriesFragment