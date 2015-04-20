package com.bitcoin.tracker.walletx.activity.txCategories;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.TxCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by menace on 4/18/2015.
 */
public class TxCategoriesAdapter extends ArrayAdapter<TxCategoriesListItem> {

    private final Activity activity;
    private ArrayList<TxCategoriesListItem> mItemsArrayList;

    private View mRowView;
    private LayoutInflater inflater;

    private TextView mGroupName;
    private TextView mDefaultGroup;
    private ImageButton mMoveDown;
    private ImageButton mMoveUp;

    public TxCategoriesAdapter(Activity activity, ArrayList<TxCategoriesListItem> itemsArrayList){
        super(activity, R.layout.fragment_txcategory, itemsArrayList);
        this.activity = activity;
        this.mItemsArrayList = itemsArrayList;
    }

    public void updateData(ArrayList<TxCategoriesListItem> itemsArrayList){
        this.mItemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        getViewsById(parent);
        setupTextLabels(position);
        return mRowView;
    }

    private void getViewsById(ViewGroup parent){
        mRowView = inflater.inflate(R.layout.fragment_txcategory_list_item, parent, false);
        mGroupName = (TextView) mRowView.findViewById(R.id.txCatName);
    }

    private void setupTextLabels(int position){
        mGroupName.setText(mItemsArrayList.get(position).getName());
        /*TxCategory current = TxCategory.getByDisplayOrder(position + 1);
        if(!current.isDefault()) {
            mDefaultGroup.setVisibility(View.GONE);
        }*/
    }

    private void refreshListView(ViewGroup parent, TxCategory updatedCat){
        AbsListView parentListView = (AbsListView) parent.findViewById(R.id.listView);
        int firstVisible = parentListView.getFirstVisiblePosition();
        int lastVisible = parentListView.getLastVisiblePosition();

        mItemsArrayList.clear();
        List<TxCategory> groups = TxCategory.getAllSortedByName();
        for(TxCategory group : groups){
            TxCategoriesListItem item;
            item = new TxCategoriesListItem(group.name);
            mItemsArrayList.add(item);

        }
        notifyDataSetChanged();

        parentListView.setSelection(firstVisible);
    }

}
