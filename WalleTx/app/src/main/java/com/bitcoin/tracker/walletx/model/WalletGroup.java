package com.bitcoin.tracker.walletx.model;

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

    //region WALLET GROUP MODEL

    // Group name.
    @Column(name = "Name",   // group names are
            unique = true,   // unique and
            notNull = true)  // cannot be empty
    public String name;

    // Default group. Has value 1, all others 0.
    @Column(name = "DefaultGroup")
    private int defaultGroup;

    public int getDefaultGroup() {
        return defaultGroup;
    }

    /**
     * Sets the default wallet group.
     */
    public void setAsDefault(int isDefault) {
        if (isDefault == 1) {
            // clear current default group and set this as default
            List<WalletGroup> groups = WalletGroup.getAll();
            for (WalletGroup group : groups) {
                if (group.defaultGroup == 1) {
                    group.defaultGroup = 0;
                    group.save();
                }
            }
            defaultGroup = 1;
        } else {
            defaultGroup = 0;
        }
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

    public WalletGroup(String name, int displayOrder) {
        super();
        this.name = name;
        this.displayOrder = displayOrder;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Dumps the WalletGroups table to console.
     * For debugging purposes only.
     */
    public static void dump() {
        String dividerCol1 = "------------------";
        String dividerCol23 = "-------------";
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        System.out.printf("%-20s %-15s %-16s\n", "Name", "DefaultGroup", "DisplayOrder");
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        List<WalletGroup> groups = WalletGroup.getAll();
        for (WalletGroup group : groups) {
            System.out.printf(
                    "%-20s %-15s %-16s\n",
                    group.name,
                    group.defaultGroup,
                    group.displayOrder);
        }
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
    }

    //endregion

    //region WALLET GROUP QUERIES

    /**
     * @param name
     * @return WalletGroup selected by name
     */
    public static WalletGroup getBy(String name) {
        return new Select()
                .from(WalletGroup.class)
                .where("Name = ?", name)
                .executeSingle();
    }

    /**
     * @return List of all WalletGroups in order.
     */
    public static List<WalletGroup> getAll() {
        return new Select()
                .from(WalletGroup.class)
                .orderBy("DisplayOrder ASC")
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

    /**
     * @return true is WalletGroup is the default group
     */
    public boolean isDefault() {
        return this.defaultGroup == 1;
    }

    public static WalletGroup getLast() {
        return new Select()
                .from(WalletGroup.class)
                .orderBy("DisplayOrder DESC")
                .executeSingle();
    }

    /**
     * @param displayOrder
     * @return Returns all WalletGroups with a displayOrder value > displayOrder param
     */
    public static List<WalletGroup> getAllWithDisplayOrderGreaterThan(int displayOrder) {
        return new Select()
                .from(WalletGroup.class)
                .where("DisplayOrder > ?", displayOrder)
                .orderBy("DisplayOrder ASC")
                .execute();
    }

    /**
     * @param displayOrder
     * @return WalletGroup with display order equal to displayOrder
     */
    public static WalletGroup getByDisplayOrder(int displayOrder) {
        return new Select()
                .from(WalletGroup.class)
                .where("DisplayOrder = ?", displayOrder)
                .executeSingle();
    }

    //endregion

} // WalletGroup
