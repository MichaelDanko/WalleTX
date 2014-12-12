package com.bitcoin.tracker.walletx.model.wallet;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.bitcoin.tracker.walletx.model.balance.Balance;
import com.bitcoin.tracker.walletx.model.tx.Tx;

import java.util.List;

/**
 * Walletx model.

 * Walletx objects are central to the application as they represents
 * the different wallets added by the user.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "Walletx")
public class Walletx extends Model {

    @Column(name = "Name")
    String name;

    @Column(name = "WalletType")
    public WalletType type;

    // Belongs to one WalletGroup (mandatory)
    @Column(name = "WalletGroup")
    public WalletGroup group;

    // Has many Txs
    public List<Tx> txs() {
        return getMany(Tx.class, "Walletx");
    }

    // Has many Balances
    public List<Balance> balances() {
        return getMany(Balance.class, "Walletx");
    }

    public Walletx() {
        super();
    }

    public Walletx(String name, WalletType type, WalletGroup group) {
        super();
        this.name = name;
        this.type = type;
        this.group = group;
    }

    /*-------------------*
     *  Walletx Queries  *
     *-------------------*/






} // Walletx