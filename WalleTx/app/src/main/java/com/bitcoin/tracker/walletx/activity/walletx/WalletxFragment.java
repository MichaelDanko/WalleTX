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

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.activity.SyncableFragmentInterface;
import com.bitcoin.tracker.walletx.activity.group.GroupUpdateActivity;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;
import com.bitcoin.tracker.walletx.api.SyncManager;
import com.bitcoin.tracker.walletx.activity.summary.SummaryAllActivity;
import com.bitcoin.tracker.walletx.activity.summary.SummaryGroupActivity;
import com.bitcoin.tracker.walletx.activity.summary.SummarySingleActivity;
import com.bitcoin.tracker.walletx.helper.Formatter;
import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.ExchangeRate;
import com.bitcoin.tracker.walletx.model.Group;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * WalletxFragment acts as the home view for the application.
 * Displays aggregations of wallets.
 */
public class WalletxFragment extends Fragment implements SyncableFragmentInterface {

    ExpandableListView mExpListView;
    WalletxExpListAdapter mListApapter;
    List<String> mGroupHeader;
    HashMap<String, List<String>> mListDataChild;
    View mHeader; // all wallets header for exp. list view
    View mFooter; // no wallets footer (shown only when no wallets are added)

    // Returns a new instance of this fragment for the given section number.
    public static WalletxFragment newInstance(int sectionNumber) {
        WalletxFragment fragment = new WalletxFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WalletxFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.walletx_fragment, container, false);
        setupExpListView(view);
        setupAllWalletsHeader();
        setupNoWalletsFooter();
        prepareData();
        populateListViewWithPreparedData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // This is the fragment that displays BTC values converted to USD,
        // so it is logical to place an exchange rate sync here.
        SyncManager.syncExchangeRate(getActivity().getApplicationContext());
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
        ((MainActivity) activity).onSectionAttached(getArguments().
                getInt(Constants.ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RESULT_WALLETX_FRAGMENT_NEW_WTX_ADDED) {
            if (resultCode == Activity.RESULT_OK) {
                refreshUi();
                Walletx wtx = SharedData.ADDED_WTX;
                SyncManager.syncNewWallet(getActivity().getApplicationContext(), wtx);
            }
        } else if (requestCode == Constants.RESULT_WALLETX_FRAGMENT_UPDATES_MADE) {
            if (resultCode == Activity.RESULT_OK) {
                refreshUi();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.walletx, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_wallet) {
            Intent intent = new Intent( getActivity(), WalletxCreateActivity.class );
            startActivityForResult( intent, Constants.RESULT_WALLETX_FRAGMENT_NEW_WTX_ADDED);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshUi() {
        prepareData();
        setHeaderFooterVisibility();
        mListApapter.updateData(mGroupHeader, mListDataChild);
    }

    private void setupExpListView(View view) {
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
            allWalletsBalance = allWalletsBalance + Balance.getBalanceAsLong(wtx);
        allWalletxBtcBalance.setText(Formatter.btcToString(allWalletsBalance));
        float inUsd = ExchangeRate.convert(allWalletsBalance, getActivity());
        allWalletsUSD.setText(Formatter.usdToString(inUsd));
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

    // Determines visibility of the list view header/footer
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
            SharedData.WTXS_TO_SUMMARIZE = Walletx.getAll();
            Intent intent = new Intent( getActivity(), SummaryAllActivity.class );
            startActivity(intent);
        }
    };

    private View.OnClickListener footerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getActivity(), WalletxCreateActivity.class );
            startActivityForResult( intent, Constants.RESULT_WALLETX_FRAGMENT_NEW_WTX_ADDED);
        }
    };

    private ExpandableListView.OnGroupClickListener groupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            TextView tv = (TextView) v.findViewById(R.id.groupName);
            String name = tv.getText().toString();
            Group group = Group.getBy(name);
            SharedData.WTXS_TO_SUMMARIZE = group.walletxs();
            Intent intent = new Intent( getActivity(), SummaryGroupActivity.class );
            startActivity( intent );
            return true;
        }
    };

    private ExpandableListView.OnChildClickListener childWalletClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick (ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            TextView tv = (TextView) v.findViewById(R.id.walletName);
            Intent intent = new Intent( getActivity(), SummarySingleActivity.class );
            String name = tv.getText().toString();
            SharedData.WTXS_TO_SUMMARIZE = new ArrayList<>();
            SharedData.WTXS_TO_SUMMARIZE.add(Walletx.getBy(name));
            startActivity(intent);
            return true;
        }
    };

    private ExpandableListView.OnItemLongClickListener childWalletLongClickListener = new ExpandableListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            int itemType = ExpandableListView.getPackedPositionType(id);

            if (Walletx.isEmpty()) {

                // do nothing if there are no wallets added
                return true;

            } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

                // open new activity to edit single walletx
                TextView tv = (TextView) view.findViewById(R.id.walletName);
                String name = tv.getText().toString();
                Intent intent = new Intent( getActivity(), WalletxUpdateActivity.class );
                intent.putExtra(Constants.EXTRA_WTX_SELECTED_TO_EDIT, name);
                startActivityForResult( intent, Constants.RESULT_WALLETX_FRAGMENT_UPDATES_MADE );
                return true;

            } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

                // open activity to update the wallet group name
                TextView group = (TextView) view.findViewById(R.id.groupName);
                String name = group.getText().toString();
                Intent intent = new Intent( getActivity(), GroupUpdateActivity.class );
                intent.putExtra(Constants.EXTRA_GROUP_SELECTED_TO_EDIT, name);
                startActivityForResult( intent, Constants.RESULT_WALLETX_FRAGMENT_UPDATES_MADE );
                return true;

            } else {
                return false;
            }
        }
    };

} // WalletxFragment