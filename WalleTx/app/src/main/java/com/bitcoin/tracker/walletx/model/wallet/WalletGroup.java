package com.bitcoin.tracker.walletx.model.wallet;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Manages wallet groups
 */
@Table(name = "WalletGroup")
public class WalletGroup extends Model {

    //region WALLET GROUP TABLE

    // Group name.
    @Column(name = "Name",   // group names are
            unique = true,   // unique and
            notNull = true)  // cannot be empty
    public String name;

    // Default group. Has value 1, all others 0.
    @Column(name = "DefaultGroup")
    private int defaultGroup;

    /**
     * Sets the default wallet group.
     */
    public void setAsDefault() {
        // clear current default group
        List<WalletGroup> groups = WalletGroup.getAll();
        for (WalletGroup group : groups) {
            if (group.defaultGroup == 1) {
                group.defaultGroup = 0;
                group.save();
            }
        }
        // set this as default
        defaultGroup = 1;
    }

    // Display order.
    @Column(name = "DisplayOrder")
    public int displayOrder;

    // Has many Walletxs
    public List<Walletx> walletxs() {
        return getMany(Walletx.class, "WalletGroup");
    }

    public WalletGroup() {
        super();
    }

    public WalletGroup(String name, int defaultGroup, int displayOrder) {
        super();
        this.name = name;
        this.defaultGroup = defaultGroup;
        this.displayOrder = displayOrder;
    }

    //endregion

    //region WALLET GROUP QUERIES

    /**
     * @return List of all WalletGroups
     */
    public static List<WalletGroup> getAll() {
        return new Select()
                .from(WalletGroup.class)
                .execute();
    }

    /**
     * @return Current default WalletGroup
     */
    public static WalletGroup getDefault() {
        return new Select()
                .from(WalletGroup.class)
                .where("DefaultGroup = ?", 1)
                .executeSingle();
    }

    //endregion

} // WalletGroup
