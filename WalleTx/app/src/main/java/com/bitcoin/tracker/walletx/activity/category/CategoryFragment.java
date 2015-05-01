package com.bitcoin.tracker.walletx.activity.category;

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

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;
import com.bitcoin.tracker.walletx.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * TxCategoriesFragment allows user to perform
 * CRUD operations on tx categories.
 *
 */
public class CategoryFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final int NEW_TAG_ADDED = 1;
    private static final int TX_CAT_UPDATED = 2;

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    // Reference to activity
    static Activity mActivity;

    // displays when no tags present
    private View mFooter;

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItems = new ArrayList<>();

    private int mRestorePosition;

    // Returns a new instance of this fragment for the given section number.
    public static CategoryFragment newInstance(int sectionNumber) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {}

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
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        mActivity = getActivity();

        mListView = (ListView) view.findViewById(R.id.listView);

        View footer = getActivity().getLayoutInflater().inflate(R.layout.category_fragment_list_footer, null);
        mListView.addFooterView(footer);
        mFooter = footer.findViewById(R.id.no_wallets_container);
        mFooter.setOnClickListener(footerOnClickListener);
        setFooterVisibility();

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        return view;
    }

    private void prepareData(){
        mItems.clear();
        List<Category> categories = Category.getAllSortedByName();
        for (Category category : categories) {
            mItems.add(category.name);
        }
    }

    private void setFooterVisibility() {
        List<Category> cats = Category.getAll();
        if (cats.size() == 0) {
            mFooter.setVisibility(View.VISIBLE);
        } else {
            mFooter.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener footerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getActivity(), CategoryCreateActivity.class );
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
        ((SyncableActivity) getActivity()).stopSyncIconRotation(); // COMMENT
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        if(null != mListener){
            String catName =(String)parent.getItemAtPosition(position);
            mListener.onFragmentInteraction(catName);
            Intent intent = new Intent(getActivity(), CategoryUpdateActivity.class);
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
        inflater.inflate(R.menu.category, menu);
    }

    /**
     * Handles action bar menu interactions for this fragment.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {


            // TODO Let SyncAct handle
            //new SyncDatabase(this);


            return true;
        } else if (item.getItemId() == R.id.action_add_tx_category) {
            Intent intent = new Intent( getActivity(), CategoryCreateActivity.class );
            startActivityForResult( intent, NEW_TAG_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

} // TxCategoriesFragment