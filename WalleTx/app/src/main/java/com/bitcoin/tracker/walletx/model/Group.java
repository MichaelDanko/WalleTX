package com.bitcoin.tracker.walletx.model;

import android.content.Context;
import android.widget.Toast;

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

    //region SCHEMA
    //
    //  Define all WalletGroup fields and relationships here.
    //  Include any getter and setter methods required for private fields.
    //

    /**
     * The name of the wallet group.
     */
    @Column(name = "Name",   // group names are
            unique = true,   // unique and
            notNull = true)  // cannot be empty
    public String name;

    /**
     * An integer used as a boolean to track if the wallet group is the default group.
     * The default group has a value of 1, all others 0.
     */
    @Column(name = "DefaultGroup")
    private int defaultGroup;

    /**
     * Returns 1 if this WalletGroup is the default group, otherwise returns 0.
     * @return int defaultGroup
     */
    public int getDefaultGroup() {
        return defaultGroup;
    }

    /**
     * Sets a WalletGroup's default value and updates the default value of other WalletGroups
     * @param isDefault boolean value defining if group is the default group
     */
    private void setAsDefault(boolean isDefault) {
        // set this groups default value to 0
        if (isDefault) {
            // clear current default group and set this group as default
            Group currentDefault = Group.getDefault();
            if (currentDefault != null) {
                currentDefault.defaultGroup = 0;
                currentDefault.save();
            }
            this.defaultGroup = 1;
        } else this.defaultGroup = 0;
    }

    /**
     * Display order tracks the order in which the wallet groups
     * will be displayed on the main activity.
     */
    @Column(name = "DisplayOrder")
    private int displayOrder;

    /**
     * Returns the display order of a WalletGroup.
     * @return int displayOrder value
     */
    public int getDisplayOrder() {
        return this.displayOrder;
    }

    /**
     * WalletGroup has a one-to-many relationship with Walletx
     * since each WalletGroup can be associated with many Walletxs.
     */
    //Has many Walletxs
    public List<Walletx> walletxs() {
        return getMany(Walletx.class, "WalletGroup");
    }

    //endregion
    //region CONSTRUCTORS
    //
    //  WalletGroup constructors
    //

    /**
     * Default constructor with no parameters.
     */
    public Group() {
        super();
    }

    //endregion
    //region CREATE
    //
    //  Include methods for creating (or adding) new WalletGroups to the WalletGroup table here
    //

    /**
     * Creates a new WalletGroup and inserts it into the WalletGroup table.
     * @param name of the new WalletGroup
     * @param isDefault boolean value determining if group should be set as default
     */
    public static void createWalletGroup(String name, boolean isDefault) {
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
     * Creates 'My Wallets' group as a default group if WalletGroup table is empty.
     * This should method is used to ensure that there is always at least
     * one wallet group in the WalletGroup table.
     * @param context application context
     */
    public static void initDefaultGroup(Context context) {
        List<Group> groups = Group.getAll();
        if ( groups.size() < 1 ) {
            Group defaultGroup = new Group();
            defaultGroup.name = context.getResources().getString(R.string.wallet_group_wtx_default_group);
            defaultGroup.displayOrder = 1;
            defaultGroup.setAsDefault(true);
            defaultGroup.save();
        }
    }

    //endregion
    //region READ
    //
    //  Include any methods for reading or accessing data from the WalletGroup table here.
    //

    /**
     * Returns WalletGroup by name.
     * @param name of the WalletGroup to be read
     * @return WalletGroup with name
     */
    public static Group getBy(String name) {
        return new Select()
                .from(Group.class)
                .where("Name = ?", name)
                .executeSingle();
    }

    /**
     * Returns all WalletGroups in no particular order.
     * @return List of all WalletGroups unsorted
     */
    public static List<Group> getAll() {
        return new Select()
                .from(Group.class)
                .execute();
    }

    /**
     * Returns all WalletGroups in ascending order by display order.
     * @return List of all WalletGroups in order by display order
     */
    public static List<Group> getAllSortedByDisplayOrder() {
        return new Select()
                .from(Group.class)
                .orderBy("DisplayOrder ASC")
                .execute();
    }

    /**
     * Returns all WalletGroups in ascending order by name.
     * @return List of all WalletGroups in order by name
     */
    public static List<Group> getAllSortedByName() {
        return new Select()
                .from(Group.class)
                .orderBy("Name ASC")
                .execute();
    }

    /**
     * Returns all WalletGroups with a display order greater than a specific value.
     * @param displayOrder select all WalletGroups after
     * @return List of all WalletGroups with a displayOrder value greater than the displayOrder parameter
     */
    public static List<Group> getAllWithDisplayOrderGreaterThan(int displayOrder) {
        return new Select()
                .from(Group.class)
                .where("DisplayOrder > ?", displayOrder)
                .orderBy("DisplayOrder ASC")
                .execute();
    }

    /**
     * Returns the current default wallet group.
     * @return WalletGroup default group
     */
    public static Group getDefault() {
        return new Select()
                .from(Group.class)
                .where("DefaultGroup = ?", 1)
                .executeSingle();
    }

    /**
     * Returns true if WalletGroup is the default group.
     * @return boolean true if group is the default.
     */
    public boolean isDefault() {
        return this.defaultGroup == 1;
    }

    /**
     * Returns the last WalletGroup, or the WalletGroup with the highest display order.
     * @return WalletGroup last group
     */
    public static Group getLast() {
        return new Select()
                .from(Group.class)
                .orderBy("DisplayOrder DESC")
                .executeSingle();
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

    //endregion
    //region UPDATE
    //
    //  Include any methods for updated WalletGroup data here.
    //

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
    //region DELETE

    /**
     * Deletes a WalletGroup from the WalletGroup table and updates the
     * display order of all other groups.
     * @param delete group to be deleted
     */
    public static void deleteGroup(Group delete) {

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

    /**
     * Returns true if the name is not an empty string and is unique
     * (i.e doesn't exist in the WalletGroup table).
     * @param context application context
     * @param name of WalletGroup
     * @return boolean value of validation results
     */
    public static boolean validate(Context context, String name) {
        return validateNameIsNotEmptyString(context, name) &&
               validateNameIsUnique(context, name);
    }

    /**
     * Validates that a group name is not an empty string.
     * @param context application context
     * @param name name of group
     * @return boolean name is valid
     */
    public static boolean validateNameIsNotEmptyString(Context context, String name) {
        if (name.isEmpty()) {
            String error = context.getString(R.string.wallet_group_create_toast_name_empty);
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * Validates that the name of group doesn't already exist in the WalletGroup table.
     * @param context application context
     * @param name name of group
     * @return boolean name is valid
     */
    public static boolean validateNameIsUnique(Context context, String name) {
        List<Group> groups = Group.getAll();
        // Cannot already exist in the database.
        for (Group group : groups) {
            if (group.name.toLowerCase().equals(name)) {
                String error = context.getString(R.string.wallet_group_create_toast_name_exists);
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    //endregion
    //region OTHER

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
