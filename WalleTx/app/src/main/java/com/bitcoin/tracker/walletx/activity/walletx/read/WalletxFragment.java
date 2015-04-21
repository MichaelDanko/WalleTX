package com.bitcoin.tracker.walletx.activity.walletx.read;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.MainActivity;
import com.bitcoin.tracker.walletx.activity.SyncableInterface;
import com.bitcoin.tracker.walletx.activity.walletGroup.updateDelete.WalletGroupUpdateActivity;
import com.bitcoin.tracker.walletx.activity.walletx.create.WalletxCreateActivity;
import com.bitcoin.tracker.walletx.activity.walletx.updateDelete.WalletxUpdateActivity;
import com.bitcoin.tracker.walletx.activity.walletxSummary.WalletxSummaryAllActivity;
import com.bitcoin.tracker.walletx.activity.walletxSummary.WalletxSummaryGroupActivity;
import com.bitcoin.tracker.walletx.activity.walletxSummary.WalletxSummarySingleActivity;
import com.bitcoin.tracker.walletx.api.BlockchainInfo;
import com.bitcoin.tracker.walletx.api.SyncDatabase;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * WalletxFragment acts as the home view for the application.
 * Displays aggregations of wallets.
 */
public class WalletxFragment extends Fragment implements SyncableInterface {

    //region FIELDS

    private static final int NEW_WALLETX_ADDED = 1;
    private static final int WALLETX_UPDATED = 2;
    private static final int WALLET_GROUP_UPDATED = 3;

    // Walletx custom expandable list
    WalletxExpandableListAdapter mListApapter;
    ExpandableListView mExpListView;
    List<String> mGroupHeader;
    HashMap<String, List<String>> mListDataChild;
    View mHeader; // all wallets header for exp. list view
    View mFooter; // no wallets footer (shown only when no wallets are added)

    // Reference to activity
    static Activity mActivity;

    // displays when sync in progress
    private ProgressBar mSyncProgressBar;

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    //endregion
    //region FRAGMENT LIFECYCLE

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

        View view = inflater.inflate(R.layout.fragment_walletx, container, false);
        mExpListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        mExpListView.setOnGroupClickListener(groupClickListener);
        mExpListView.setOnChildClickListener(childWalletClickListener);
        mExpListView.setOnItemLongClickListener(childWalletLongClickListener);

        // Add All Wallets header to expandable list view.
        View header = inflater.inflate(R.layout.fragment_walletx_list_item_all_wallets, null);
        mExpListView.addHeaderView(header);
        mHeader = header.findViewById(R.id.allWalletsContainer);
        mHeader.setOnClickListener(allWalletsOnClickListener);

        // Use the footer to display 'Add first wallet view' when no wallets are added
        View footer = getActivity().getLayoutInflater().inflate(R.layout.fragment_walletx_list_item_no_wallets_footer, null);
        mExpListView.addFooterView(footer);
        mFooter = footer.findViewById(R.id.no_wallets_container);
        mFooter.setOnClickListener(footerOnClickListener);
        setHeaderFooterVisibility();

        // setup exp list view
        prepareData();
        if (mListApapter == null)
            mListApapter = new WalletxExpandableListAdapter(getActivity(), mGroupHeader, mListDataChild);
        if (mExpListView != null)
            mExpListView.setAdapter(mListApapter);

        // setup sync progress spinner
        mSyncProgressBar = (ProgressBar) view.findViewById(R.id.syncProgressBar);
        mSyncProgressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        mSyncProgressBar.setVisibility(View.GONE);

        return view;
    }

    /**
     * Prepares wallet group / wallet data for the expandable list view.
     */
    private void prepareData() {
        mGroupHeader = new ArrayList<>();
        mListDataChild = new HashMap<String, List<String>>();

        // For each wallet group
        List<WalletGroup> groups = WalletGroup.getAllSortedByDisplayOrder();
        for (WalletGroup group : groups) {
            List<Walletx> wtxs = group.walletxs(); // get all wtxs in this group

            // Setup only if the wallet group has at least 1 wallet
            if (wtxs.size() > 0) {
                mGroupHeader.add(group.name);
                List<String> wtxsInThisGroup = new ArrayList<>(); // names of all wtx in group
                for (Walletx wtx : wtxs) {
                    wtxsInThisGroup.add(wtx.name);
                }
                mListDataChild.put(group.name, wtxsInThisGroup);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    /**
     * Determines visibility of the list view header/footer
     */
    private void setHeaderFooterVisibility() {

        // TODO @dc I need a count query here. Or isEmpty query. Replace the if statement below...
        int wtxCount = new Select()
                .from(Walletx.class)
                .count();

        if (wtxCount == 0) {
            mFooter.setVisibility(View.VISIBLE);
            mHeader.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.VISIBLE);
            mFooter.setVisibility(View.GONE);
        }
    }

    //endregion
    //region EVENT HANDLING

    private View.OnClickListener allWalletsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getActivity(), WalletxSummaryAllActivity.class );
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
            Intent intent = new Intent( getActivity(), WalletxSummaryGroupActivity.class );
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
            Intent intent = new Intent( getActivity(), WalletxSummarySingleActivity.class );
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
                Intent intent = new Intent( getActivity(), WalletGroupUpdateActivity.class );
                intent.putExtra("wallet_group_name", name);
                startActivityForResult( intent, WALLET_GROUP_UPDATED );
                return true;

            } else {

                // Should Never Happen. TODO Throw error / write to log
                return false;

            }
        }
    };

    /**
     * Refreshes the expListView and initiates a data sync
     * when changes have been made (i.e. new Walletx added or deleted)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == NEW_WALLETX_ADDED) {
            // Make sure the request was successful
            if (resultCode == getActivity().RESULT_OK) {
                // Refresh the expListView to display the newly added wallet
                prepareData();
                setHeaderFooterVisibility();
                mListApapter.updateData(mGroupHeader, mListDataChild);

                /*
                 * TODO @md Initiate a sync for the new wallet
                 *          A new wallet has been added so we'll need to initiate a data sync on
                 *          a background thread. The sync will pull all transactions and update
                 *          the Tx and Balance tables. The user should receive feedback that a
                 *          sync is occurring (I'm thinking the refresh icon in the action bar
                 *          can rotate, which as UI guy @bh will handle). Upon successfully
                 *          completion, when data is added to db, the background thread created by
                 *          the sync helper class should notify this fragment so that the
                 *          expListView can be updated to show new data.
                 *
                 *          A group discussion is probably req'd about the design of the sync
                 *          helper class.
                 *
                 *          Also, for starters we can probably begin with a single method that
                 *          updates (syncs) all Walletxs and use it here. Time permitting we
                 *          can tweak things to make it more efficient.
                 *
                 */

                Walletx wtx = Walletx.getBy(data.getStringExtra("name_of_wtx_added"));
                SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
                new BlockchainInfo(this, saw.publicKey, wtx).execute();


            }
        } else if (requestCode == WALLETX_UPDATED || requestCode == WALLET_GROUP_UPDATED) {
            if (resultCode == getActivity().RESULT_OK) {
                prepareData();
                setHeaderFooterVisibility();
                mListApapter.updateData(mGroupHeader, mListDataChild);
            }
        }
    }

    //endregion
    //region OPTIONS MENU

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Add fragment specific action bar items to activity action bar items.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            new SyncDatabase(this);
            return true;
        } else if (item.getItemId() == R.id.action_add_wallet) {
            // open new activity
            Intent intent = new Intent( getActivity(), WalletxCreateActivity.class );
            startActivityForResult( intent, NEW_WALLETX_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion
    //region SYNC

    public void startSyncRelatedUI() {
        // Rotate progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void stopSyncRelatedUI() {
        // stop progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            });
        }
        // update list view
        prepareData();
        mListApapter.updateData(mGroupHeader, mListDataChild);
    }

    //endregion

} // WalletxFragment