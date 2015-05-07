package com.bitcoin.tracker.walletx.activity.group;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Group list item
 */
public class GroupAdapter extends ArrayAdapter<String> {

    private final Activity mActivity;
    private ArrayList<String> mItems;
    private View mRowView;
    private LayoutInflater mInflater;
    private TextView mGroupName;
    private TextView mDefaultGroup;
    private ImageButton mMoveDown;
    private ImageButton mMoveUp;

    public GroupAdapter(Activity activity, ArrayList<String> itemsArrayList) {
        super(activity, R.layout.group_fragment_list_item, itemsArrayList);
        mActivity = activity;
        mItems = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        getViews(parent);
        setupMoveButtons(position);
        bindListeners(parent, position);
        setupTextLabels(position);
        return mRowView;
    }

    private void getViews(ViewGroup parent) {
        mRowView = mInflater.inflate(R.layout.group_fragment_list_item, parent, false);
        mGroupName = (TextView) mRowView.findViewById(R.id.groupNameLabel);
        mDefaultGroup = (TextView) mRowView.findViewById(R.id.defaultGroupLabel);
        mMoveDown = (ImageButton) mRowView.findViewById(R.id.imageButtonMoveDown);
        mMoveUp = (ImageButton) mRowView.findViewById(R.id.imageButtonMoveUp);
    }

    /**
     * Sets up the buttons for changing the display order.
     * TODO Replace with drag and drop handles
     */
    private void setupMoveButtons(int position) {
        // Stop buttons from preventing list item click event
        mMoveDown.setFocusable(false);
        mMoveDown.setFocusableInTouchMode(false);
        mMoveUp.setFocusable(false);
        mMoveUp.setFocusableInTouchMode(false);
        // Disable move down buttons for first list items.
        if (position == 0) {
            mMoveUp.setEnabled(false);
            mMoveUp.setVisibility(View.INVISIBLE);
        }
        // Disable move up button for last list item
        int last = Group.getLast().getDisplayOrder();
        if (position + 1 == last) {
            mMoveDown.setEnabled(false);
            mMoveDown.setVisibility(View.INVISIBLE);
            if (last == 2) mMoveDown.setVisibility(View.GONE);
        }
    }

    private void bindListeners(final ViewGroup parent, final int position) {

        mMoveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int displayOrder = position + 1;
                Group clicked = Group.getByDisplayOrder(displayOrder);
                Group swap = Group.getByDisplayOrder(displayOrder + 1);
                Group.swap(clicked, swap);
                refreshListView(parent, clicked);
            }
        });

        mMoveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int displayOrder = position + 1;
                Group clicked = Group.getByDisplayOrder(displayOrder);
                Group swap = Group.getByDisplayOrder(displayOrder - 1);
                Group.swap(clicked, swap);
                refreshListView(parent, clicked);
            }
        });

    } // bindListeners

    public void updateData(ArrayList<String> itemsArrayList) {
        this.mItems = itemsArrayList;
        notifyDataSetChanged();
    }

    private void setupTextLabels(int position) {
        mGroupName.setText(mItems.get(position));
        // Hide default label for non-default groups
        Group current = Group.getByDisplayOrder(position + 1);
        if (!current.isDefault())
            mDefaultGroup.setVisibility(View.GONE);
    }

    /**
     * Refreshes the content of the parent list view
     * while maintaining the current vertical position in the list view.
     */
    private void refreshListView(ViewGroup parent, Group updatedGroup) {

        // Get position of first and last visible list items
        AbsListView parentListView = (AbsListView) parent.findViewById(android.R.id.list);
        int firstVisible = parentListView.getFirstVisiblePosition();
        int lastVisible = parentListView.getLastVisiblePosition();

        // Populate list view
        mItems.clear();
        List<Group> groups = Group.getAllSortedByDisplayOrder();
        for (Group group : groups) {
            mItems.add(group.name);
        }
        notifyDataSetChanged();

        // Handle case where item is at visible top and moved up.
        if (firstVisible >= updatedGroup.getDisplayOrder()) {
            parentListView.setSelection(updatedGroup.getDisplayOrder() - 1);
        // Handle case where item is at visible bottom and moved down.
        } else if (lastVisible < updatedGroup.getDisplayOrder()) {
            parentListView.setSelection(firstVisible + 1);
        // Handle move cases in the middle of viewable listview.
        } else {
            parentListView.setSelection(firstVisible);
        }
    }

} // GroupAdapter
