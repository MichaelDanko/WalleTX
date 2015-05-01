package com.bitcoin.tracker.walletx.activity.walletx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.activity.group.GroupUpdateActivity;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;
import com.bitcoin.tracker.walletx.api.SyncManager;
import com.bitcoin.tracker.walletx.activity.summary.SummaryAllActivity;
import com.bitcoin.tracker.walletx.activity.summary.SummaryGroupActivity;
import com.bitcoin.tracker.walletx.activity.summary.SummarySingleActivity;
import com.bitcoin.tracker.walletx.model.ExchangeRate;
import com.bitcoin.tracker.walletx.model.Group;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * WalletxFragment acts as the home view for the application.
 * Displays aggregations of wallets.
 */
public class WalletxFragment extends Fragment {

    // onActivityResult requestCodes
    private static final int NEW_WALLETX_ADDED = 1;
    private static final int WALLETX_UPDATED = 2;
    private static final int WALLET_GROUP_UPDATED = 3;

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Walletx custom expandable list
    ExpandableListView mExpListView;
    WalletxExpListAdapter mListApapter;
    List<String> mGroupHeader;
    HashMap<String, List<String>> mListDataChild;
    View mHeader; // all wallets header for exp. list view
    View mFooter; // no wallets footer (shown only when no wallets are added)

    // Reference to activity
    private static Activity mActivity;

    // Returns a new instance of this fragment for the given section number.
    public static WalletxFragment newInstance(int sectionNumber) {
        WalletxFragment fragment = new WalletxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WalletxFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.walletx_fragment, container, false);
        getExpListViewAndBindEvents(view);
        setupAllWalletsHeader();
        setupNoWalletsFooter();
        prepareData();
        populateListViewWithPreparedData();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop the sync icon animation so it doesn't appear over any other menus
        // if syncable, the next fragment will re-apply the rotation to its own sync menu item
        ((SyncableActivity) getActivity()).stopSyncIconRotation(); // COMMENT
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    /**
     * Refreshes the expListView and initiates a data sync when required
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_WALLETX_ADDED) {
            if (resultCode == Activity.RESULT_OK) {
                refreshUi();
                Walletx wtx = SharedData.ADDED_WTX;
                SyncManager.syncNewWallet(getActivity().getApplicationContext(), wtx);
            }
        } else if (requestCode == WALLETX_UPDATED || requestCode == WALLET_GROUP_UPDATED) {
            if (resultCode == Activity.RESULT_OK) {
                refreshUi();
            }
        }
    }

    private void getExpListViewAndBindEvents(View view) {
        mExpListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        mExpListView.setOnGroupClickListener(groupClickListener);
        mExpListView.setOnChildClickListener(childWalletClickListener);
        mExpListView.setOnItemLongClickListener(childWalletLongClickListener);
    }

    private void setupAllWalletsHeader() {
        View header = getActivity().getLayoutInflater()
                .inflate(R.layout.walletx_fragment_list_header, null);
        if (mExpListView != null)
            mExpListView.addHeaderView(header);
        mHeader = header.findViewById(R.id.allWalletsContainer);
        mHeader.setOnClickListener(allWalletsOnClickListener);
    }

    private void setupNoWalletsFooter() {
        View footer = getActivity().getLayoutInflater()
                .inflate(R.layout.walletx_fragment_list_footer, null);
        if (mExpListView != null)
            mExpListView.addFooterView(footer);
        mFooter = footer.findViewById(R.id.no_wallets_container);
        mFooter.setOnClickListener(footerOnClickListener);
        setHeaderFooterVisibility();
    }

    // Prepares data for the expandable list view
    private void prepareData() {
        prepareAllWalletsHeaderData();
        prepareGroupAndChildData();
    }

    private void prepareAllWalletsHeaderData() {
        TextView allWalletxBtcBalance = (TextView) mHeader.findViewById(R.id.textView3);
        TextView allWalletsUSD = (TextView) mHeader.findViewById(R.id.textView4);
        List<Walletx> all = Walletx.getAll();
        long allWalletsBalance = 0;
        for (Walletx wtx : all)
            allWalletsBalance = allWalletsBalance + wtx.finalBalance;
        allWalletxBtcBalance.setText(Tx.formattedBTCValue(allWalletsBalance));
        String inUSD = NumberFormat.getIntegerInstance().
                format(ExchangeRate.EXCHANGE_RATE_IN_USD * allWalletsBalance / Constants.SATOSHIS);
        allWalletsUSD.setText(inUSD);
    }

    private void prepareGroupAndChildData() {
        mGroupHeader = new ArrayList<>();
        mListDataChild = new HashMap<>();
        List<Group> groups = Group.getAllSortedByDisplayOrder();
        for (Group group : groups) {
            List<Walletx> wtxs = group.walletxs();
            // Setup only if the wallet group has at least 1 wallet
            if (wtxs.size() > 0) {
                mGroupHeader.add(group.name);
                List<String> wtxsInThisGroup = new ArrayList<>(); // names of all wtx in group
                for (Walletx wtx : wtxs)
                    wtxsInThisGroup.add(wtx.name);
                mListDataChild.put(group.name, wtxsInThisGroup);
            }
        }
    }

    private void populateListViewWithPreparedData() {
        if (mListApapter == null)
            mListApapter = new WalletxExpListAdapter(getActivity(), mGroupHeader, mListDataChild);
        if (mExpListView != null)
            mExpListView.setAdapter(mListApapter);
    }

    private void refreshUi() {
        prepareData();
        setHeaderFooterVisibility();
        mListApapter.updateData(mGroupHeader, mListDataChild);
    }

    /**
     * Determines visibility of the list view header/footer
     */
    private void setHeaderFooterVisibility() {
        if (Walletx.isEmpty()) {
            mFooter.setVisibility(View.VISIBLE);
            mHeader.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.VISIBLE);
            mFooter.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener allWalletsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getActivity(), SummaryAllActivity.class );
            startActivity(intent);
        }
    };

    private View.OnClickListener footerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getActivity(), WalletxCreateActivity.class );
            startActivityForResult( intent, NEW_WALLETX_ADDED );
        }
    };

    private ExpandableListView.OnGroupClickListener groupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            TextView tv = (TextView) v.findViewById(R.id.groupName);
            Intent intent = new Intent( getActivity(), SummaryGroupActivity.class );
            intent.putExtra("group_name", tv.getText().toString());
            intent.putExtra("type", "group");
            startActivity( intent );
            return true;
        }
    };

    private ExpandableListView.OnChildClickListener childWalletClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick (ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            TextView tv = (TextView) v.findViewById(R.id.walletName);
            Intent intent = new Intent( getActivity(), SummarySingleActivity.class );
            intent.putExtra("walletx_name", tv.getText().toString());
            intent.putExtra("type", "wallet");
            startActivity(intent);
            return true;
        }
    };

    private ExpandableListView.OnItemLongClickListener childWalletLongClickListener = new ExpandableListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            int itemType = ExpandableListView.getPackedPositionType(id);

            // TODO @dc I need a count query here. Or isEmpty query. Replace the if statement below...
            int wtxCount = new Select()
                    .from(Walletx.class)
                    .count();

            if ( wtxCount == 0 ) {

                // do nothing if there are no wallets added
                return true;

            } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

                // open new activity to edit single walletx
                TextView tv = (TextView) view.findViewById(R.id.walletName);
                String name = tv.getText().toString();
                Intent intent = new Intent( getActivity(), WalletxUpdateActivity.class );
                intent.putExtra("walletx_name", name);
                startActivityForResult( intent, WALLETX_UPDATED );
                return true;

            } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

                // open activity to update the wallet group name
                TextView group = (TextView) view.findViewById(R.id.groupName);
                String name = group.getText().toString();
                Intent intent = new Intent( getActivity(), GroupUpdateActivity.class );
                intent.putExtra("wallet_group_name", name);
                startActivityForResult( intent, WALLET_GROUP_UPDATED );
                return true;

            } else {

                // Should Never Happen. TODO Throw error / write to log
                return false;

            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Add fragment specific action bar items to activity action bar items.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.walletx, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println("WTF?");

        if (item.getItemId() == R.id.action_add_wallet) {
            // open new activity
            Intent intent = new Intent( getActivity(), WalletxCreateActivity.class );
            startActivityForResult( intent, NEW_WALLETX_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

} // WalletxFragment