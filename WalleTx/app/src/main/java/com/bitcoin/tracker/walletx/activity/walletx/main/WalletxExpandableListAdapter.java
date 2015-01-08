package com.bitcoin.tracker.walletx.activity.walletx.main;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;

/**
 * http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */
public class WalletxExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mGroupHeader;
    private HashMap<String, List<String>> mSingleWalletChild;

    public WalletxExpandableListAdapter(Context context,
                                 List<String> groupHeader,
                                 HashMap<String, List<String>> singleWalletChild) {
        this.mContext = context;
        this.mGroupHeader = groupHeader;
        this.mSingleWalletChild = singleWalletChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mSingleWalletChild.get(this.mGroupHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_walletx_list_item_single_wallet, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.walletName);
        TextView description = (TextView) convertView.findViewById(R.id.walletDescription);
        TextView btcBalance = (TextView) convertView.findViewById(R.id.btcBalance);
        TextView lcBalance = (TextView) convertView.findViewById(R.id.lcBalance);

        //-------------------
        // TODO Modify text
        //-------------------

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mSingleWalletChild.get(this.mGroupHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mGroupHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mGroupHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_walletx_list_item_group, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.groupName);
        TextView btcBalance = (TextView) convertView.findViewById(R.id.btcBalance);
        TextView lcBalance = (TextView) convertView.findViewById(R.id.lcBalance);

        //-------------------
        // TODO Modify text
        // lblListHeader.setTypeface(null, Typeface.BOLD);
        // lblListHeader.setText(headerTitle);
        //-------------------

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
