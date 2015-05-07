package com.bitcoin.tracker.walletx.activity.category;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.activity.SyncableFragmentInterface;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;
import com.bitcoin.tracker.walletx.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Presents a Category ListView so that user can create, update and delete Categories.
 */
public class CategoryFragment extends Fragment implements
        AbsListView.OnItemClickListener,
        SyncableFragmentInterface {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItems = new ArrayList<>(); // data
    private int mRestorePosition;

    /**
     * The footer of the ListView displays a 'no tags present' message
     * and is only visible when no Category has been added by user.
     */
    private View mFooter;

    public static CategoryFragment newInstance(int sectionNumber) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(mAdapter == null){
            prepareData();
            mAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, mItems);
        } else if (mRestorePosition != 0) {
            mListView.setSelection(mRestorePosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        setupListViewFooter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((MainActivity) activity).onSectionAttached(getArguments().
                getInt(Constants.ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause(){
        super.onResume();
        mRestorePosition = mListView.getFirstVisiblePosition(); // save last visible position
        // stop the sync icon animation so it doesn't appear over any other menus
        // if syncable, the next fragment will re-apply the rotation to its own sync menu item
        ((SyncableActivity) getActivity()).stopSyncIconRotation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.RESULT_CATEGORY_CHANGES_MADE) {
            if(resultCode == Activity.RESULT_OK) {
                prepareData();
                setFooterVisibility();
                mAdapter.notifyDataSetChanged();
                if(mRestorePosition != 0)
                    mListView.setSelection(mRestorePosition);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        if(null != mListener){
            String name = (String) parent.getItemAtPosition(position);
            mListener.onFragmentInteraction(name);
            Intent intent = new Intent(getActivity(), CategoryUpdateActivity.class);
            intent.putExtra(Constants.EXTRA_CATEGORY_SELECTED_TO_EDIT, name);
            startActivityForResult(intent, Constants.RESULT_CATEGORY_CHANGES_MADE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Add fragment specific action bar items to activity action bar items.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_tx_category) {
            Intent intent = new Intent( getActivity(), CategoryCreateActivity.class );
            startActivityForResult( intent, Constants.RESULT_CATEGORY_CHANGES_MADE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshUi() {
        // Required SyncableFragmentInterface method.
        // We want the user to be able to initiate a sync from the MainActivity; however,
        // a sync will no change data for this fragment. Thus we do nothing here.
    }

    private void setupListViewFooter() {
        if (mFooter == null) {
            View footer = getActivity().getLayoutInflater().
                    inflate(R.layout.category_fragment_list_footer, null);
            mListView.addFooterView(footer);
            mFooter = footer.findViewById(R.id.no_wallets_container);
            mFooter.setOnClickListener(footerOnClickListener);
        }
        setFooterVisibility();
    }

    private void prepareData(){
        mItems.clear();
        List<Category> categories = Category.getAllSortedByName();
        for (Category category : categories)
            mItems.add(category.name);
    }

    private void setFooterVisibility() {
        if (Category.nonePresent())
            mFooter.setVisibility(View.VISIBLE);
        else
            mFooter.setVisibility(View.GONE);
    }

    private View.OnClickListener footerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent( getActivity(), CategoryCreateActivity.class );
            startActivityForResult( intent, Constants.RESULT_CATEGORY_CHANGES_MADE);
        }
    };

    public interface OnFragmentInteractionListener{
        public void onFragmentInteraction(String id);
    }

} // CategoryFragment