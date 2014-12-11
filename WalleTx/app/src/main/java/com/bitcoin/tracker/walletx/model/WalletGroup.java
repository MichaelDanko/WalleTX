package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * WalletGroup model.
 *
 * A WalletGroup is nothing more than a group name that is applied
 * to various Walletx's.
 *
 */
@Table(name = "WalletGroup")
public class WalletGroup extends Model {

    @Column(name = "Name")
    public String name;

    // Has many Walletxs
    public List<Walletx> walletxs() {
        return getMany(Walletx.class, "WalletGroup");
    }

    public WalletGroup() {
        super();
    }

    public WalletGroup(String name) {
        super();
        this.name = name;
    }

    /*-----------------------*
     *  WalletGroup Queries  *
     *-----------------------*/




} // WalletGroup
