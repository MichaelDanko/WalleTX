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

    @Column(name = "Group")
    public WalletGroup group; // Walletx has a group

    public Walletx() {
        super();
    }

    public Walletx(String name, WalletType type, WalletGroup group) {
        super();
        this.name = name;
        this.type = type;
        this.group = group;
    }

    // Walletx queries

    //public static Walletx getRandom() {
    //    return new Select().from(Walletx.class).orderBy("RANDOM()").executeSingle();
    //}

} // Walletx
