package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * TxCategory model.
 *
 * TxCategory is simply a category name that can be applied to
 * individual transactions.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "TxCategory")
public class TxCategory extends Model {

    @Column(name = "Name")
    public String name;

    // Has many Tx's
    public List<Tx> txs() {
        return getMany(Tx.class, "TxCategory");
    }

    public TxCategory() {
        super();
    }

    public TxCategory(String name) {
        super();
        this.name = name;
    }

    /*----------------------*
     *  TxCategory Queries  *
     *----------------------*/



} // TxCategory
