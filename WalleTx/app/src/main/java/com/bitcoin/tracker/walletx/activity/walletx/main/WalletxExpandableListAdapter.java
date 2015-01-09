package com.bitcoin.tracker.walletx.activity.walletx.main;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Sets up the expandable list view on the main WalleTx fragment.
 * Reference: http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
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
        return this.mSingleWalletChild.get(this.mGroupHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_walletx_list_item_single_wallet, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.walletName);
        TextView description = (TextView) convertView.findViewById(R.id.walletDescription);
        TextView btcBalance = (TextView) convertView.findViewById(R.id.btcBalance);
        TextView btcBalanceLabel = (TextView) convertView.findViewById(R.id.btcCurrencyLabel);
        btcBalanceLabel.setTypeface(null, Typeface.ITALIC);
        TextView lcBalance = (TextView) convertView.findViewById(R.id.lcLabel);
        TextView lcBalanceLabel = (TextView) convertView.findViewById(R.id.lcLabel);
        lcBalanceLabel.setTypeface(null, Typeface.ITALIC);

        final String walletName = (String) getChild(groupPosition, childPosition);
        Walletx wtx = Walletx.getBy(walletName);

        name.setText(wtx.name);
        if (wtx.type.equals(WalletType.SINGLE_ADDRESS_WALLET)) {
            // Get the SAWallet associated with this WTX
            SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
            if (saw != null)
                description.setText(saw.publicKey);
        }

        //------------------------
        // TODO Modify Balances
        //------------------------

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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_walletx_list_item_group, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.groupName);
        TextView btcBalance = (TextView) convertView.findViewById(R.id.btcBalance);
        btcBalance.setTypeface(null, Typeface.BOLD_ITALIC);
        TextView btcBalanceLabel = (TextView) convertView.findViewById(R.id.btcCurrencyLabel);
        btcBalanceLabel.setTypeface(null, Typeface.ITALIC);
        TextView lcBalance = (TextView) convertView.findViewById(R.id.lcBalance);
        lcBalance.setTypeface(null, Typeface.BOLD_ITALIC);
        TextView lcBalanceLabel = (TextView) convertView.findViewById(R.id.lcLabel);
        lcBalanceLabel.setTypeface(null, Typeface.ITALIC);

        String headerTitle = (String) getGroup(groupPosition);
        WalletGroup group = WalletGroup.getBy(headerTitle);
        name.setText(group.name);

        //-------------------
        // TODO Modify BTC and LC balances for this group
        //-------------------

        // Do we want groups to remain expanded?
        ExpandableListView elv = (ExpandableListView) parent;
        elv.expandGroup(groupPosition);

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
