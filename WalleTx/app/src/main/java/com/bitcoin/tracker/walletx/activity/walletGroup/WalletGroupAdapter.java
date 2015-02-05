package com.bitcoin.tracker.walletx.activity.walletGroup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.WalletGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Populates custom list items in the WalletGroup list view.
 */
public class WalletGroupAdapter extends ArrayAdapter<WalletGroupListItem> {

    //region FIELDS

    private final Activity activity;
    private final ArrayList<WalletGroupListItem> itemsArrayList;

    private View mRowView;
    private LayoutInflater inflater;

    private TextView mGroupName;
    private TextView mDefaultGroup;
    private ImageButton mMoveDown;
    private ImageButton mMoveUp;

    //endregion
    //region ADAPTER

    public WalletGroupAdapter(Activity activity, ArrayList<WalletGroupListItem> itemsArrayList) {
        super(activity, R.layout.fragment_walletgroup_list_item, itemsArrayList);
        this.activity = activity;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        getViewsById(parent);
        setupMoveButtons(position);
        bindClickListeners(parent, position);
        setupTextLabels(position);
        return mRowView;
    }

    private void getViewsById(ViewGroup parent) {
        mRowView = inflater.inflate(R.layout.fragment_walletgroup_list_item, parent, false);
        mGroupName = (TextView) mRowView.findViewById(R.id.groupNameLabel);
        mDefaultGroup = (TextView) mRowView.findViewById(R.id.defaultGroupLabel);
        mMoveDown = (ImageButton) mRowView.findViewById(R.id.imageButtonMoveDown);
        mMoveUp = (ImageButton) mRowView.findViewById(R.id.imageButtonMoveUp);
    }

    /**
     * Sets up the buttons for changing the display order.
     */
    private void setupMoveButtons(int position) {
        // Stop buttons from preventing list item click event
        // http://stackoverflow.com/questions/11160639/list-item-with-button-not-clickable-anymore
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
        int last = WalletGroup.getLast().getDisplayOrder();
        if (position + 1 == last) {
            mMoveDown.setEnabled(false);
            mMoveDown.setVisibility(View.INVISIBLE);
            if (last == 2) mMoveDown.setVisibility(View.GONE);
        }
    }

    private void setupTextLabels(int position) {
        mGroupName.setText(itemsArrayList.get(position).getName());

        // Hide default label for non-default groups
        WalletGroup current = WalletGroup.getByDisplayOrder(position + 1);
        if (!current.isDefault()) {
            mDefaultGroup.setVisibility(View.GONE);
        }
    }

    //endregion
    //region EVENT HANDLING

    /**
     * Binds on click listener to the move up and move down display order buttons.
     * @param parent listview
     * @param position within list
     */
    private void bindClickListeners(final ViewGroup parent, final int position) {

        mMoveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int displayOrder = position + 1;
                WalletGroup clicked = WalletGroup.getByDisplayOrder(displayOrder);
                WalletGroup swap = WalletGroup.getByDisplayOrder(displayOrder + 1);
                WalletGroup.swap(clicked, swap);
                refreshListView(parent, clicked);
            }

        });

        mMoveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int displayOrder = position + 1;
                WalletGroup clicked = WalletGroup.getByDisplayOrder(displayOrder);
                WalletGroup swap = WalletGroup.getByDisplayOrder(displayOrder - 1);
                WalletGroup.swap(clicked, swap);
                refreshListView(parent, clicked);
            }
        });
    }

    /**
     * Refreshes the content of the parent list view
     * while maintaining the current vertical position in the list view.
     * Reference: http://stackoverflow.com/questions/22474779/listview-resets-after-update
     * @param parent listview
     */
    private void refreshListView(ViewGroup parent, WalletGroup updatedGroup) {

        // Get position of first and last visible list items
        AbsListView parentListView = (AbsListView) parent.findViewById(android.R.id.list);
        int firstVisible = parentListView.getFirstVisiblePosition();
        int lastVisible = parentListView.getLastVisiblePosition();

        // Populate list view
        ListAdapter adapter;
        ArrayList<WalletGroupListItem> items = new ArrayList<>();
        List<WalletGroup> groups = WalletGroup.getAllSortedByDisplayOrder();
        for (WalletGroup group : groups) {
            WalletGroupListItem item;
            item = new WalletGroupListItem(group.name);
            items.add(item);
        }
        adapter = new WalletGroupAdapter(activity, items);
        ((AdapterView<ListAdapter>) parent).setAdapter(adapter);

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

    //endregion
}
