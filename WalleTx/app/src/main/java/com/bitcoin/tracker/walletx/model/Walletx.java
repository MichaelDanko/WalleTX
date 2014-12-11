package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Walletx table
 *
 */
@Table(name = "Walletx")
public class Walletx extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "Type")
    public WalletType type;

    @Column(name = "Group") // Walletx has one group
    public WalletGroup group;

    // TODO: Has many Txs

    // TODO: Has many Balances

    public Walletx() {
        super();
    }

    public Walletx(String name, WalletType type, WalletGroup group) {
        super();
        this.name = name;
        this.type = type;
        this.group = group;
    }

    /*
     * Walletx Queries
     *
     */






} // Walletx
