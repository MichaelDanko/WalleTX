package com.bitcoin.tracker.walletx.activity.walletGroup;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.MainActivity;
import com.bitcoin.tracker.walletx.model.WalletGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of all wallet groups.
 *
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class WalletGroupFragment extends Fragment implements AbsListView.OnItemClickListener {

    // The fragment argument representing the section number for this fragment.
    // Used to communicate to the MainActivity that WalletGroupFragment is currently active.
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int argSectionNumber;

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    // Tracks the position of the listView such that it can be restored.
    private int mRestorePosition;

    // Tracks number of items in the list view
    private int mListViewCount;

    public static WalletGroupFragment newInstance(int sectionNumber) {
        WalletGroupFragment fragment = new WalletGroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalletGroupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            argSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        setupAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_walletgroup, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    private void setupAdapter() {

        ArrayList<WalletGroupListItem> items = new ArrayList<>();
        List<WalletGroup> groups = WalletGroup.getAll();
        for (WalletGroup group : groups) {
            WalletGroupListItem item;
            item = new WalletGroupListItem(group.name);
            items.add(item);
        }
        mAdapter = new WalletGroupAdapter(getActivity(), items);

        if (mRestorePosition != 0)
            mListView.setSelection(mRestorePosition);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        // Tells the main activity which fragment is active so that the
        // action bar title can be updated.
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Refreshes the list view to reflect changes.
     */
    @Override
    public void onResume() {
        super.onResume();
        setupAdapter();
        mListView.setAdapter(mAdapter);

        // Restores previous listView position.
        if (mRestorePosition != 0)
            mListView.setSelection(mRestorePosition);

        // Goes to end of listView if new group was added.
        if (mListViewCount == mListView.getCount() - 1) {
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    @Override
    public void onPause() {
        super.onResume();
        mRestorePosition = mListView.getFirstVisiblePosition(); // save last visible position
        mListViewCount = mListView.getCount(); // increment count
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            String name = WalletGroup.getAll().get(position).toString();

            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(name);

            // Open update wallet activity.
            Intent intent = new Intent( getActivity(), WalletGroupUpdateActivity.class );
            intent.putExtra("wallet_group_name", name);
            startActivity(intent);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

    //region OPTIONS MENU

    /**
     * Adds fragment specific menu options to action bar menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.groups, menu);
    }

    /**
     * Handles action bar menu interactions for this fragment.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_group) {
            Intent intent = new Intent( getActivity(), WalletGroupCreateActivity.class );
            startActivity( intent );
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion
}
