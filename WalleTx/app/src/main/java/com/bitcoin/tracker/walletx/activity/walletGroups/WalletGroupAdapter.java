package com.bitcoin.tracker.walletx.activity.walletGroups;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.wallet.WalletGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Populates custom list items in the WalletGroup list view.
 */
public class WalletGroupAdapter extends ArrayAdapter<WalletGroupListItem> {

    private final Activity activity;
    private final ArrayList<WalletGroupListItem> itemsArrayList;
    private TextView mGroupName;
    private TextView mDefaultGroup;
    private ImageButton mMoveDown;
    private ImageButton mMoveUp;
    private View mRowView;
    private LayoutInflater inflater;

    public WalletGroupAdapter(Activity activity, ArrayList<WalletGroupListItem> itemsArrayList) {
        super(activity, R.layout.fragment_walletgroup_list_item, itemsArrayList);
        this.activity = activity;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Create inflater
        inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        getViewsById(parent);
        setupMoveButtons(position);
        bindClickListeners(parent, position);
        setupTextLabels(position);
        return mRowView;
    }

    /**
     * Gets rowView form inflater.
     * @param parent
     */
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
        int last = WalletGroup.getLast().displayOrder;
        if (position + 1 == last) {
            mMoveDown.setEnabled(false);
            mMoveDown.setVisibility(View.INVISIBLE);
            if (last == 2) mMoveDown.setVisibility(View.GONE);
        }
    }

    /**
     * Binds on click listener to the move up and move down display order buttons.
     * @param parent
     * @param position
     */
    private void bindClickListeners(final ViewGroup parent, final int position) {

        mMoveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int displayOrder = position + 1;
                WalletGroup clicked = WalletGroup.getByDisplayOrder(displayOrder);
                WalletGroup swap = WalletGroup.getByDisplayOrder(displayOrder + 1);
                swap.displayOrder = displayOrder;
                clicked.displayOrder = displayOrder + 1;
                swap.save();
                clicked.save();
                refreshListView(parent);
                WalletGroup.dump();
            }

        });

        mMoveUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int displayOrder = position + 1;
                WalletGroup clicked = WalletGroup.getByDisplayOrder(displayOrder);
                WalletGroup swap = WalletGroup.getByDisplayOrder(displayOrder - 1);
                swap.displayOrder = displayOrder;
                clicked.displayOrder = displayOrder - 1;
                swap.save();
                clicked.save();
                refreshListView(parent);
                WalletGroup.dump();
            }
        });
    }

    private void setupTextLabels(int position) {
        // Add group name
        mGroupName.setText(itemsArrayList.get(position).getName());
        // Setup default label
        WalletGroup current = WalletGroup.getByDisplayOrder(position + 1);
        if (!current.isDefault()) {
            mDefaultGroup.setVisibility(View.GONE);
        }
    }

    /**
     * Refreshes the content of the parent list view
     * @param parent
     */
    private void refreshListView(ViewGroup parent) {

        //--------------------------------------------------------------
        // TODO Ensure that list view refreshes to last known position.
        //--------------------------------------------------------------

        ListAdapter adapter;
        ArrayList<WalletGroupListItem> items = new ArrayList<WalletGroupListItem>();
        List<WalletGroup> groups = WalletGroup.getAll();
        for (WalletGroup group : groups) {
            WalletGroupListItem item;
            item = new WalletGroupListItem(group.name);
            items.add(item);
        }
        adapter = new WalletGroupAdapter(activity, items);
        ((AdapterView<ListAdapter>) parent).setAdapter(adapter);
    }
}
