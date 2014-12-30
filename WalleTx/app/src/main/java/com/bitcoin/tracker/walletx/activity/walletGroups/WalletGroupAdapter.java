package com.bitcoin.tracker.walletx.activity.walletGroups;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;

import java.util.ArrayList;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        // Create inflater
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Get rowView from inflater
        View rowView = inflater.inflate(R.layout.fragment_walletgroup_list_item, parent, false);
        TextView groupName = (TextView) rowView.findViewById(R.id.groupNameLabel);
        TextView defaultGroup = (TextView) rowView.findViewById(R.id.defaultGroupLabel);

        // Set the text for textView
        groupName.setText(itemsArrayList.get(position).getName());
        defaultGroup.setText(itemsArrayList.get(position).getIsDefault());

        // Return rowView
        return rowView;
    }
}