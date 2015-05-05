package com.bitcoin.tracker.walletx.model;

import android.content.Context;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.bitcoin.tracker.walletx.R;

import java.util.List;

/**
 * A WalletGroup is nothing more than a name that can be associated with Walletx
 * objects in order to aggregate their data.
 */
@Table(name = "WalletGroup")
public class Group extends Model {

    //region TABLE
    //----------------------------------------------------------------------------------------------

    @Column(name = "Name", unique = true, notNull = true)
    public String name;

    @Column(name = "DefaultGroup")
    private int defaultGroup;

    // Using this method without calling save will result in no default group - Use save!
    private void setAsDefault(boolean yes) {
        if (yes) {
            // clear current default group and set this group as default
            Group currentDefault = Group.getDefault();
            if (currentDefault != null) {
                currentDefault.defaultGroup = 0;
                currentDefault.save();
            }
            this.defaultGroup = 1;
        } else {
            this.defaultGroup = 0;
        }
    }

    @Column(name = "DisplayOrder")
    private int displayOrder;

    public int getDisplayOrder() {
        return this.displayOrder;
    }

    //Has many Walletxs
    public List<Walletx> walletxs() {
        return getMany(Walletx.class, "WalletGroup");
    }

    public Group() {
        super();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Creates 'My Wallets' group as a default group if WalletGroup table is empty.
     * This should method is used to ensure that there is always at least
     * one wallet group in the WalletGroup table.
     * @param context application context
     */
    public static void initDefaultGroup(Context context) {
        List<Group> groups = Group.getAll();
        if ( groups.size() < 1 ) {
            Group defaultGroup = new Group();
            defaultGroup.name = context.getResources().
                    getString(R.string.group_wtx_default_group);
            defaultGroup.displayOrder = 1;
            defaultGroup.setAsDefault(true);
            defaultGroup.save();
        }
    }

    /**
     * Swaps the display order of 2 WalletGroups
     * @param wg1 WalletGroup 1
     * @param wg2 WalletGroup 2
     */
    public static void swap(Group wg1, Group wg2) {
        int temp = wg1.getDisplayOrder();
        wg1.displayOrder = wg2.getDisplayOrder();
        wg2.displayOrder = temp;
        wg1.save();
        wg2.save();
    }

    //endregion
    //region QUERIES
    //----------------------------------------------------------------------------------------------

    /**
     * Creates a new Group and inserts it into the Group table.
     * @param name of the new Group
     * @param isDefault boolean value determining if group should be set as default
     */
    public static void create(String name, boolean isDefault) {
        Group newGroup = new Group();
        newGroup.name = name;
        // Set group display order and save.
        Group last = Group.getLast();
        newGroup.displayOrder = last.getDisplayOrder() + 1;
        // Set group as default.
        newGroup.setAsDefault(isDefault);
        newGroup.save();
    }

    /**
     * Returns Group by name.
     * @param name of the WalletGroup to be read
     * @return WalletGroup with name
     */
    public static Group getBy(String name) {
        return new Select().from(Group.class).where("Name = ?", name).executeSingle();
    }

    /**
     * Returns all Groups in no particular order.
     * @return List of all WalletGroups unsorted
     */
    public static List<Group> getAll() {
        return new Select().from(Group.class).execute();
    }

    /**
     * Returns all Groups in ascending order by display order.
     * @return List of all WalletGroups in order by display order
     */
    public static List<Group> getAllSortedByDisplayOrder() {
        return new Select().from(Group.class).orderBy("DisplayOrder ASC").execute();
    }

    /**
     * Returns all Groups in ascending order by name.
     * @return List of all WalletGroups in order by name
     */
    public static List<Group> getAllSortedByName() {
        return new Select().from(Group.class).orderBy("Name ASC").execute();
    }

    /**
     * Returns all Groups with a display order greater than a specific value.
     * @param value select all Groups after
     * @return List of all Groups with a displayOrder value greater than the value parameter
     */
    public static List<Group> getAllWithDisplayOrderGreaterThan(int value) {
        return new Select()
                .from(Group.class)
                .where("DisplayOrder > ?", value)
                .orderBy("DisplayOrder ASC")
                .execute();
    }

    /**
     * Returns the current default wallet group.
     * @return WalletGroup default group
     */
    public static Group getDefault() {
        return new Select().from(Group.class).where("DefaultGroup = ?", 1).executeSingle();
    }

    /**
     * Returns true if Group is the default group.
     * @return boolean true if group is the default.
     */
    public boolean isDefault() {
        return this.defaultGroup == 1;
    }

    /**
     * Returns the last Group (highest display order).
     * @return Group last group
     */
    public static Group getLast() {
        return new Select().from(Group.class).orderBy("DisplayOrder DESC").executeSingle();
    }

    /**
     * Returns the WalletGroup with a specific display order.
     * @param displayOrder of WalletGroup to be returned
     * @return WalletGroup with display order
     */
    public static Group getByDisplayOrder(int displayOrder) {
        return new Select()
                .from(Group.class)
                .where("DisplayOrder = ?", displayOrder)
                .executeSingle();
    }

    /**
     * Updates a WalletGroups name and default status.
     * @param name of group
     * @param isDefault boolean where true sets to default
     */
    public void update(String name, boolean isDefault) {
        this.name = name;
        this.setAsDefault(isDefault);
        this.save();
    }

    /**
     * Updates the WalletGroup's name and saves.
     * @param name updated name
     */
    public void updateName(String name) {
        this.name = name;
        this.save();
    }

    /**
     * Updates the WalletGroup's defaultGroup field and saves.
     * @param isDefault boolean where true sets to default
     */
    public void updateDefault(boolean isDefault) {
        this.setAsDefault(isDefault);
        this.save();
    }

    /**
     * Deletes a WalletGroup from the WalletGroup table and updates the
     * display order of all other groups.
     * @param delete group to be deleted
     */
    public static void delete(Group delete) {

        /*
         * ---------------------------------------------------------------------------------------------------------
         * TODO @dc Verify that this functionality is implemented correctly once Walletx implementation is complete.
         * ---------------------------------------------------------------------------------------------------------
         */
        List<Walletx> wallets = delete.walletxs();
        Group defaultGroup = Group.getDefault();
        for (Walletx wtx : wallets) {
            if (wtx.group == delete) {
                wtx.group = defaultGroup;
                wtx.save();
            }
        }

        // Change the display order of groups after the deleted group
        List<Group> groups = Group.getAllWithDisplayOrderGreaterThan(delete.getDisplayOrder());
        for (Group group : groups) {
            group.displayOrder = group.getDisplayOrder() - 1;
            group.save();
        }

        delete.delete();
    }

    //endregion
    //region VALIDATE
    //----------------------------------------------------------------------------------------------

    /**
     * @param name Group name to validate
     * @return true if name is empty string
     */
    public static boolean isEmptyString(String name) {
        return name.equals("");
    }

    /**
     * @param name Group name to validate
     * @return true if Group name matches an existing name
     */
    public static boolean matchesExisting(String name) {
        Group existing = Group.getBy(name);
        return existing != null ? true : false;
    }

    /**
     * Validates that a Group name is unique, excluding comparison
     * to the Group being updated.
     *
     * @param name Group name to validate
     * @param updating Group being updating
     * @return true if Category name matches an existing name that is not self
     */
    public static boolean matchesExisting(String name, Group updating) {
        Group existing = Group.getBy(name);
        if (existing != null && existing != updating)
            return true;
        return false;
    }

    //endregion
    //region DEBUG
    //----------------------------------------------------------------------------------------------

    public static void dump() {
        String dividerCol1 = "------------------";
        String dividerCol23 = "-------------";
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        System.out.printf("%-20s %-15s %-16s\n", "Name", "DefaultGroup", "DisplayOrder");
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        List<Group> groups = Group.getAllSortedByDisplayOrder();
        for (Group group : groups) {
            System.out.printf(
                    "%-20s %-15s %-16s\n",
                    group.name,
                    group.defaultGroup,
                    group.displayOrder);
        }
    }

    //endregion

} // WalletGroup
