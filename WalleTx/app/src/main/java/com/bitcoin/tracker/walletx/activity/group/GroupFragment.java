package com.bitcoin.tracker.walletx.activity.group;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ProgressBar;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;
import com.bitcoin.tracker.walletx.api.SyncableInterface;
import com.bitcoin.tracker.walletx.api.SyncDatabase;
import com.bitcoin.tracker.walletx.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a list of all wallet groups.
 *
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 */
public class GroupFragment extends Fragment implements
        AbsListView.OnItemClickListener,
        SyncableInterface {

    //region FIELDS

    private static final int NEW_GROUP_ADDED = 1;
    private static final int WALLET_GROUP_UPDATED = 2;

    // The fragment argument representing the section number for this fragment.
    // Used to communicate to the MainActivity that WalletGroupFragment is currently active.
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Reference to activity
    static Activity mActivity;

    // displays when sync in progress
    private ProgressBar mSyncProgressBar;

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private GroupAdapter mAdapter;
    private ArrayList<GroupListItem> mItems = new ArrayList<>();

    // Tracks the position of the listView such that it can be restored.
    private int mRestorePosition;

    //endregion
    //region FRAGMENT LIFECYCLE

    public static GroupFragment newInstance(int sectionNumber) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        if (mAdapter == null) {
            prepareData();
            mAdapter = new GroupAdapter(getActivity(), mItems);
        }
        if (mRestorePosition != 0)
            mListView.setSelection(mRestorePosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // added _list
        View view = inflater.inflate(R.layout.group_fragment, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        // setup sync progress spinner
        mSyncProgressBar = (ProgressBar) view.findViewById(R.id.syncProgressBar);
        mSyncProgressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        mSyncProgressBar.setVisibility(View.GONE);

        return view;
    }

    private void prepareData() {
        mItems.clear();
        List<Group> groups = Group.getAllSortedByDisplayOrder();
        for (Group group : groups) {
            GroupListItem item;
            item = new GroupListItem(group.name);
            mItems.add(item);
        }
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

    @Override
    public void onPause() {
        super.onResume();
        mRestorePosition = mListView.getFirstVisiblePosition(); // save last visible position
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            String name = Group.getAllSortedByDisplayOrder().get(position).toString();

            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(name);

            // Open update wallet activity.
            Intent intent = new Intent( getActivity(), GroupUpdateActivity.class );
            intent.putExtra("wallet_group_name", name);
            startActivityForResult(intent, WALLET_GROUP_UPDATED);
        }
    }

    //endregion
    //region EVENT HANDLING

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_GROUP_ADDED) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {
                // Refresh the expListView to display the newly added wallet
                prepareData();
                mAdapter.updateData(mItems);
                // Goes to end of listView if new group was added.
                mListView.setSelection(mListView.getCount());
            }
        } else if (requestCode == WALLET_GROUP_UPDATED) {
            if (resultCode == getActivity().RESULT_OK) {
                prepareData();
                mAdapter.updateData(mItems);
                // Restores previous listView position.
                if (mRestorePosition != 0)
                    mListView.setSelection(mRestorePosition);
            }
        }
    }

    //endregion
    //region OPTIONS MENU

    /**
     * Adds fragment specific menu options to action bar menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.group, menu);
    }

    /**
     * Handles action bar menu interactions for this fragment.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            new SyncDatabase(this);
            return true;
        } else if (item.getItemId() == R.id.action_add_group) {
            Intent intent = new Intent( getActivity(), GroupCreateActivity.class );
            startActivityForResult( intent, NEW_GROUP_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion
    //region SYNC

    public void startSyncRelatedUI() {
        // Rotate progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void stopSyncRelatedUI() {
        // stop progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            });
        }
    }

    //endregion
}
