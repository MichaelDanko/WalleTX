package com.bitcoin.tracker.walletx.activity.txCategories;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.MainActivity;
import com.bitcoin.tracker.walletx.activity.SyncableInterface;
import com.bitcoin.tracker.walletx.activity.txCategories.TxCategoryCreateActivity;
import com.bitcoin.tracker.walletx.activity.txCategories.TxCategoryUpdateActivity;
import com.bitcoin.tracker.walletx.activity.txCategories.TxCategoriesFragment;
import com.bitcoin.tracker.walletx.api.SyncDatabase;
import com.bitcoin.tracker.walletx.model.TxCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * TxCategoriesFragment allows user to perform
 * CRUD operations on tx categories.
 *
 */
public class TxCategoriesFragment extends Fragment implements
        AbsListView.OnItemClickListener,
        SyncableInterface {

    private static final int NEW_TAG_ADDED = 1;
    private static final int TX_CAT_UPDATED = 2;

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Reference to activity
    static Activity mActivity;

    // displays when sync in progress
    private ProgressBar mSyncProgressBar;

    // displays when no tags present
    private View mFooter;

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItems = new ArrayList<>();

    private int mRestorePosition;

    // Returns a new instance of this fragment for the given section number.
    public static TxCategoriesFragment newInstance(int sectionNumber) {
        TxCategoriesFragment fragment = new TxCategoriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TxCategoriesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(mAdapter==null){
            prepareData();
            mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mItems);
        }
        if(mRestorePosition != 0){
            mListView.setSelection(mRestorePosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_txcategory, container, false);
        mActivity = getActivity();

        mListView = (ListView) view.findViewById(R.id.listView);

        View footer = getActivity().getLayoutInflater().inflate(R.layout.fragment_txcategory_no_tags_footer, null);
        mListView.addFooterView(footer);
        mFooter = footer.findViewById(R.id.no_wallets_container);
        mFooter.setOnClickListener(footerOnClickListener);
        setFooterVisibility();

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        // setup sync progress spinner
        mSyncProgressBar = (ProgressBar) view.findViewById(R.id.syncProgressBar);
        mSyncProgressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        mSyncProgressBar.setVisibility(View.GONE);

        return view;
    }

    private void prepareData(){
        mItems.clear();
        List<TxCategory> categories = TxCategory.getAllSortedByName();
        for (TxCategory category : categories) {
            mItems.add(category.name);
        }
    }

    private void setFooterVisibility() {
        List<TxCategory> cats = TxCategory.getAll();
        if (cats.size() == 0) {
            mFooter.setVisibility(View.VISIBLE);
        } else {
            mFooter.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener footerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getActivity(), TxCategoryCreateActivity.class );
            startActivityForResult( intent, NEW_TAG_ADDED );
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause(){
        super.onResume();
        mRestorePosition = mListView.getFirstVisiblePosition();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        if(null != mListener){
            String catName =(String)parent.getItemAtPosition(position);
            mListener.onFragmentInteraction(catName);
            Intent intent = new Intent(getActivity(), TxCategoryUpdateActivity.class);
            intent.putExtra("txcategory_name", catName);
            startActivityForResult(intent, TX_CAT_UPDATED);
        }
    }

    public interface OnFragmentInteractionListener{
        public void onFragmentInteraction(String id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == NEW_TAG_ADDED){
            if(resultCode == getActivity().RESULT_OK){
                prepareData();
                setFooterVisibility();
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mListView.getCount());
            }
        } else if (requestCode == TX_CAT_UPDATED){
            if(resultCode == getActivity().RESULT_OK){
                prepareData();
                setFooterVisibility();
                mAdapter.notifyDataSetChanged();
                if(mRestorePosition != 0){
                    mListView.setSelection(mRestorePosition);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Add fragment specific action bar items to activity action bar items.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.categories, menu);
    }

    /**
     * Handles action bar menu interactions for this fragment.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            new SyncDatabase(this);
            return true;
        } else if (item.getItemId() == R.id.action_add_tx_category) {
            Intent intent = new Intent( getActivity(), TxCategoryCreateActivity.class );
            startActivityForResult( intent, NEW_TAG_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

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
    }

    //endregion

} // TxCategoriesFragment