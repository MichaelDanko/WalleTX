package com.bitcoin.tracker.walletx.activity.txCategories;

/**
 * Created by menace on 4/18/2015.
 */
public class TxCategoriesListItem {
    private String name;

    public TxCategoriesListItem(String name)
    {
        super();
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
