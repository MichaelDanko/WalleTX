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

    public WalletGroupAdapter(Activity activity, ArrayList<WalletGroupListItem> itemsArrayList) {
        super(activity, R.layout.fragment_walletgroup_list_item, itemsArrayList);
        this.activity = activity;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Create inflater
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Get rowView from inflater
        View rowView = inflater.inflate(R.layout.fragment_walletgroup_list_item, parent, false);
        TextView groupName = (TextView) rowView.findViewById(R.id.groupNameLabel);
        TextView defaultGroup = (TextView) rowView.findViewById(R.id.defaultGroupLabel);
        ImageButton moveDown = (ImageButton) rowView.findViewById(R.id.imageButtonMoveDown);
        ImageButton moveUp = (ImageButton) rowView.findViewById(R.id.imageButtonMoveUp);

        // Stop buttons from preventing list item click event
        // http://stackoverflow.com/questions/11160639/list-item-with-button-not-clickable-anymore
        moveDown.setFocusable(false);
        moveDown.setFocusableInTouchMode(false);
        moveUp.setFocusable(false);
        moveUp.setFocusableInTouchMode(false);

        // Disable the up and down buttons for the first and last list items.
        if (position == 0) {
            moveUp.setEnabled(false);
            moveUp.setVisibility(View.INVISIBLE);
        }
        if (position + 1 == WalletGroup.getLast().displayOrder) {
            moveDown.setEnabled(false);
            moveDown.setVisibility(View.INVISIBLE);
        }

        // Set the text for textView
        groupName.setText(itemsArrayList.get(position).getName());
        defaultGroup.setText(itemsArrayList.get(position).getIsDefault());

        // On click move the wallet group up or down and change its displayOrder.
        moveDown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO ADD THIS FUNCTIONALITY
                System.out.println("MOVE DOWN");
            }

        });

        moveUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int displayOrder = position + 1;
                WalletGroup clicked = new Select()
                        .from(WalletGroup.class)
                        .where("DisplayOrder = ?", position + 1)
                        .executeSingle();
                WalletGroup swap = new Select()
                        .from(WalletGroup.class)
                        .where("DisplayOrder = ?", position)
                        .executeSingle();
                swap.displayOrder = position + 1;
                clicked.displayOrder = position;
                swap.save();
                clicked.save();

                refreshListView(parent);


                WalletGroup.dump();

            }
        });

        // Return rowView
        return rowView;
    }

    /**
     * Refreshes the content of the parent list view
     * @param parent
     */
    private void refreshListView(ViewGroup parent) {
        ListAdapter adapter;
        ArrayList<WalletGroupListItem> items = new ArrayList<WalletGroupListItem>();
        List<WalletGroup> groups = WalletGroup.getAll();
        for (WalletGroup group : groups) {
            WalletGroupListItem item;
            // Hide form components.
            if (group.isDefault()) {
                item = new WalletGroupListItem(group.name,
                        activity.getString(R.string.label_default_wallet_group));
            } else {
                item = new WalletGroupListItem(group.name, "");
            }
            items.add(item);
        }
        adapter = new WalletGroupAdapter(activity, items);
        ((AdapterView<ListAdapter>) parent).setAdapter(adapter);
    }

}
