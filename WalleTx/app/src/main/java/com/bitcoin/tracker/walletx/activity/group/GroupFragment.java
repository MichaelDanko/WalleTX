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
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.activity.SyncableFragmentInterface;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;

import com.bitcoin.tracker.walletx.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Presents a Group ListView so that user can create, update, sort and delete Groups.
 */
public class GroupFragment extends Fragment implements
        AbsListView.OnItemClickListener,
        SyncableFragmentInterface {

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private GroupAdapter mAdapter;
    private ArrayList<String> mItems = new ArrayList<>();
    private int mRestorePosition;

    public static GroupFragment newInstance(int sectionNumber) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mAdapter == null) {
            prepareData();
            mAdapter = new GroupAdapter(getActivity(), mItems);
        } else if (mRestorePosition != 0) {
            mListView.setSelection(mRestorePosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
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
                getArguments().getInt(Constants.ARG_SECTION_NUMBER));
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
        // stop the sync icon animation so it doesn't appear over any other menus
        // if syncable, the next fragment will re-apply the rotation to its own sync menu item
        ((SyncableActivity) getActivity()).stopSyncIconRotation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RESULT_GROUP_ADDED) {
            if (resultCode == getActivity().RESULT_OK) {
                prepareData();
                mAdapter.updateData(mItems);
                // go to end of listView to display new group
                mListView.setSelection(mListView.getCount());
            }
        } else if (requestCode == Constants.RESULT_GROUP_UPDATED) {
            if (resultCode == getActivity().RESULT_OK) {
                prepareData();
                mAdapter.updateData(mItems);
                // restore previous listView position.
                if (mRestorePosition != 0)
                    mListView.setSelection(mRestorePosition);
            }
        }
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
            intent.putExtra(Constants.EXTRA_GROUP_SELECTED_TO_EDIT, name);
            startActivityForResult(intent, Constants.RESULT_GROUP_UPDATED);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.group, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_group) {
            Intent intent = new Intent( getActivity(), GroupCreateActivity.class );
            startActivityForResult( intent, Constants.RESULT_GROUP_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshUi() {
        // Required SyncableFragmentInterface method.
        // We want the user to be able to initiate a sync from the MainActivity; however,
        // a sync will no change data for this fragment. Thus we do nothing here.
    }

    private void prepareData() {
        mItems.clear();
        List<Group> groups = Group.getAllSortedByDisplayOrder();
        for (Group group : groups) {
            mItems.add(group.name);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }

} // GroupFragment
