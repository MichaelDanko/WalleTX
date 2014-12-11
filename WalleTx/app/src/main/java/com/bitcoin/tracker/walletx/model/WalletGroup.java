package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * WalletGroup table
 *
 */
@Table(name = "WalletGroup")
public class WalletGroup extends Model {

    @Column(name = "Name")
    public String name;

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

} // WalletGroup
