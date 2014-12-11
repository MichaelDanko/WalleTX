package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * TxCategory table
 *
 */
@Table(name = "TxCategory")
public class TxCategory extends Model {

    @Column(name = "Name")
    public String name;

    public List<Tx> txs() {
        return getMany(Tx.class, "TxCategory"); // TxCategory has many Tx's
    }

    public TxCategory() {
        super();
    }

    public TxCategory(String name) {
        super();
        this.name = name;
    }

    /*
     * TxCategory Queries
     *
     */



} // TxCategory
