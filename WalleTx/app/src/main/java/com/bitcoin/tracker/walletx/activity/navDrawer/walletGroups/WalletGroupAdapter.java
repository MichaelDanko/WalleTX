package com.bitcoin.tracker.walletx.activity.navDrawer.walletGroups;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;

import java.util.ArrayList;

/**
 *
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

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.fragment_walletgroup_list_item, parent, false);

        TextView labelView = (TextView) rowView.findViewById(R.id.groupName);
        TextView valueView = (TextView) rowView.findViewById(R.id.testView);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getName());
        valueView.setText(itemsArrayList.get(position).getTest());

        // 5. retrn rowView
        return rowView;
    }
}