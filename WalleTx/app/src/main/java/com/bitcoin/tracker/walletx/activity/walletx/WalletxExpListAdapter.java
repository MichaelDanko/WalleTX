package com.bitcoin.tracker.walletx.activity.walletx;

import java.text.NumberFormat;
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
import com.bitcoin.tracker.walletx.model.ExchangeRate;
import com.bitcoin.tracker.walletx.model.Group;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.SupportedWalletType;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Sets up the expandable list view on the main WalleTx fragment.
 * Reference: http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */
public class WalletxExpListAdapter extends BaseExpandableListAdapter {

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

    public WalletxExpListAdapter(Context context,
                                 List<String> groupHeader,
                                 HashMap<String, List<String>> singleWalletChild) {
        this.mContext = context;
        this.mGroupHeader = groupHeader;
        this.mSingleWalletChild = singleWalletChild;
    }

    public void updateData(List<String> groupHeader,
                                  HashMap<String, List<String>> singleWalletChild) {
        mGroupHeader = groupHeader;
        mSingleWalletChild = singleWalletChild;
        notifyDataSetChanged();
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
            convertView = infalInflater.inflate(R.layout.wallets_fragment_list_item, null);
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
        if (wtx.type.equals(SupportedWalletType.SINGLE_ADDRESS_WALLET)) {
            // Get the SAWallet associated with this WTX
            SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
            if (saw != null)
                mDescription.setText(saw.publicKey);
        }

        try {
            mBtcBalance.setText(new Tx().formattedBTCValue(wtx.finalBalance));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String inUSD = NumberFormat.getIntegerInstance().format(ExchangeRate.EXCHANGE_RATE_IN_USD * wtx.finalBalance / 100000000);
        mLocalCurrencyBalance.setText(inUSD);

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
            convertView = inflater.inflate(R.layout.walletx_fragment_group_header, null);
        }
        getViewsInGroupHeader(convertView);
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
        mLocalCurrencyBalance = (TextView) convertView.findViewById(R.id.lcBalance);
        mLocalCurrencyBalanceLabel = (TextView) convertView.findViewById(R.id.lcBalance);
    }

    private void prepareGroupHeaderData(int groupPosition, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        Group group = Group.getBy(headerTitle);
        mName.setText(group.name);

        // Update btc balance
        List<Walletx> all = Walletx.getAll();
        long groupBalance = 0;
        for (Walletx wtx : all) {
            if (wtx.group.name.equals(group.name)) {
                groupBalance = groupBalance + wtx.finalBalance;
            }
        }
        mBtcBalance.setText(Tx.formattedBTCValue(groupBalance));
        String inUSD = NumberFormat.getIntegerInstance().format(ExchangeRate.EXCHANGE_RATE_IN_USD * groupBalance / 100000000);
        mLocalCurrencyBalance.setText(inUSD);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //endregion
}
