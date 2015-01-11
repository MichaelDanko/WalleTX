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

    //region FIELDS

    private Context mContext;
    private List<String> mGroupHeader;
    private HashMap<String, List<String>> mSingleWalletChild;

    // Views within the list items.
    TextView mName;
    TextView mDescription; // wallet only
    TextView mBtcBalance;
    TextView mBtcBalanceLabel;
    TextView mLocalCurrencyBalance;
    TextView mLocalCurrencyBalanceLabel;

    //endregion

    public WalletxExpandableListAdapter(Context context,
                                        List<String> groupHeader,
                                        HashMap<String, List<String>> singleWalletChild) {
        this.mContext = context;
        this.mGroupHeader = groupHeader;
        this.mSingleWalletChild = singleWalletChild;
    }

    //region CHILD ROWS (Rows pertaining to wallets within groups)

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
        getViewsInChild(convertView);
        styleViewsInChild();
        prepareChildData(groupPosition, childPosition);

        return convertView;
    }

    private void getViewsInChild(View convertView) {
        mName = (TextView) convertView.findViewById(R.id.walletName);
        mDescription = (TextView) convertView.findViewById(R.id.walletDescription);
        mBtcBalance = (TextView) convertView.findViewById(R.id.btcBalance);
        mBtcBalanceLabel = (TextView) convertView.findViewById(R.id.btcCurrencyLabel);
        mLocalCurrencyBalance = (TextView) convertView.findViewById(R.id.lcBalance);
        mLocalCurrencyBalanceLabel = (TextView) convertView.findViewById(R.id.lcLabel);
    }

    private void styleViewsInChild() {
        mBtcBalanceLabel.setTypeface(null, Typeface.ITALIC);
        mLocalCurrencyBalanceLabel.setTypeface(null, Typeface.ITALIC);
    }

    private void prepareChildData(int groupPosition, int childPosition) {
        final String walletName = (String) getChild(groupPosition, childPosition);
        Walletx wtx = Walletx.getBy(walletName); // get walletx by mName
        mName.setText(wtx.name);
        if (wtx.type.equals(WalletType.SINGLE_ADDRESS_WALLET)) {
            // Get the SAWallet associated with this WTX
            SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
            if (saw != null)
                mDescription.setText(saw.publicKey);
        }

        //------------------------
        // TODO Modify Balances
        //------------------------
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mSingleWalletChild.get(this.mGroupHeader.get(groupPosition)).size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //endregion
    //region GROUP (Group header rows)

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
        getViewsInGroupHeader(convertView);
        styleViewsInGroupHeader();
        prepareGroupHeaderData(groupPosition, parent);

        // Prevent group collapsing
        ExpandableListView elv = (ExpandableListView) parent;
        elv.expandGroup(groupPosition);
        elv.setGroupIndicator(null);

        return convertView;
    }

    private void getViewsInGroupHeader(View convertView) {
        mName = (TextView) convertView.findViewById(R.id.groupName);
        mBtcBalance = (TextView) convertView.findViewById(R.id.btcBalance);
        mBtcBalanceLabel = (TextView) convertView.findViewById(R.id.btcCurrencyLabel);
        mLocalCurrencyBalance = (TextView) convertView.findViewById(R.id.lcLabel);
        mLocalCurrencyBalanceLabel = (TextView) convertView.findViewById(R.id.lcLabel);
    }

    private void styleViewsInGroupHeader() {
        mBtcBalanceLabel.setTypeface(null, Typeface.ITALIC);
        mBtcBalance.setTypeface(null, Typeface.BOLD_ITALIC);
        mLocalCurrencyBalance.setTypeface(null, Typeface.BOLD_ITALIC);
        mLocalCurrencyBalanceLabel.setTypeface(null, Typeface.ITALIC);
    }

    private void prepareGroupHeaderData(int groupPosition, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        WalletGroup group = WalletGroup.getBy(headerTitle);
        mName.setText(group.name);

        //-------------------------------------------------
        // TODO Modify BTC and LC balances for this group
        //-------------------------------------------------
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //endregion
}
