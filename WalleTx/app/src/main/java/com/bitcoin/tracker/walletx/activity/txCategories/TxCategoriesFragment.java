package com.bitcoin.tracker.walletx.activity.txCategories;

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
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.MainActivity;
import com.bitcoin.tracker.walletx.activity.txCategories.TxCategoryCreateActivity;
import com.bitcoin.tracker.walletx.activity.txCategories.TxCategoryUpdateActivity;
import com.bitcoin.tracker.walletx.activity.txCategories.TxCategoriesFragment;
import com.bitcoin.tracker.walletx.model.TxCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * TxCategoriesFragment allows user to perform
 * CRUD operations on tx categories.
 *
 * TODO @as populate the list view from fragment_txcategory.xml
 * TODO @as after new tx cat has been added, repopulate list view to display
 * TODO @as bind click event to action bar menu / list items
 *          that open either create or update activity
 *
 *
 */
public class TxCategoriesFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final int NEW_TAG_ADDED = 1;
    private static final int TX_CAT_UPDATED = 2;

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private TxCategoriesAdapter mAdapter;
    private ArrayList<TxCategoriesListItem> mItems = new ArrayList<>();

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
            mAdapter = new TxCategoriesAdapter(getActivity(),mItems);
        }
        if(mRestorePosition != 0){
            mListView.setSelection(mRestorePosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_txcategory,container,false);

        mListView = (AbsListView)view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);
        return view;
    }

    private void prepareData(){
        mItems.clear();
        List<TxCategory> groups = TxCategory.getAllSortedByName();

        for (TxCategory group:groups){
            TxCategoriesListItem item;
            item = new TxCategoriesListItem(group.name);
            mItems.add(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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
            String name = TxCategory.getAllSortedByName().get(position).toString();
            mListener.onFragmentInteraction(name);

            Intent intent = new Intent(getActivity(), TxCategoryUpdateActivity.class);
            intent.putExtra("txcategory_category_name", name);
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
                mAdapter.updateData(mItems);
                mListView.setSelection(mListView.getCount());
            }
        }else if(requestCode == TX_CAT_UPDATED){
            if(resultCode == getActivity().RESULT_OK){
                prepareData();
                mAdapter.updateData(mItems);
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
        if (item.getItemId() == R.id.action_add_tx_category) {
            Intent intent = new Intent( getActivity(), TxCategoryCreateActivity.class );
            startActivityForResult( intent, NEW_TAG_ADDED );
        }
        return super.onOptionsItemSelected(item);
    }

} // TxCategoriesFragment