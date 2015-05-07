package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * TxCategory model.
 *
 * TxCategory is simply a category name that can be applied to
 * individual transactions.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "TxCategory")
public class Category extends Model {

    //region TABLE
    //----------------------------------------------------------------------------------------------

    @Column(name = "Name")
    public String name = "Uncategorized";

    // Has many Tx's
    public List<Tx> txs() {
        return getMany(Tx.class, "TxCategory");
    }

    public Category() {
        super();
    }

    public Category(String name) {
        super();
        this.name = new String(name);
    }

    public String toString(){
        return name;
    }

    //endregion
    //region QUERIES
    //----------------------------------------------------------------------------------------------

    /**
     * Inserts a new category
     * @param name of the new category
     */
    public static void create(String name) {
        if (!isEmptyString(name)) {
            Category category = new Category(name);
            category.save();
        }
    }

    public static Category getBy(String name) {
        return new Select()
                .from(Category.class)
                .where("Name = ?", name)
                .executeSingle();
    }

    public static List<Category> getAll() {
        return new Select().from(Category.class).execute();
    }

    public static List<Category> getAllSortedByName() {
        return new Select()
                .from(Category.class)
                .orderBy("Name ASC")
                .execute();
    }

    public void update(String name) {
        this.name = name;
        this.save();
    }

    public static void delete(Category category) {
        List<Tx> txs = category.txs();
        for (Tx tx : txs) {
            tx.category = null;
            tx.save();
        }
        category.delete();
    }

    /**
     * @return true if there are no Categories
     */
    public static boolean nonePresent() {
        int count = new Select().from(Category.class).count();
        return count <= 0;
    }

    //endregion
    //region VALIDATION
    //----------------------------------------------------------------------------------------------

    /**
     * @param name Category name to validate
     * @return true if name is empty string
     */
    public static boolean isEmptyString(String name) {
        return name.equals("");
    }

    /**
     * @param name Category name to validate
     * @return true if Category name matches an existing name
     */
    public static boolean matchesExisting(String name) {
        Category existing = Category.getBy(name);
        return existing != null ? true : false;
    }

    /**
     * Validates that a Category name is unique, excluding comparison
     * to the Category being updated.
     *
     * @param name Category name to validate
     * @param updating Category being updating
     * @return true if Category name matches an existing name that is not self
     */
    public static boolean matchesExisting(String name, Category updating) {
        Category existing = Category.getBy(name);
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
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1);
        System.out.printf("%-20s %-15s %-16s\n", "Name");
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1);
        List<Category> groups = Category.getAll();
        for (Category group : groups) {
            System.out.printf(
                    "%-20s %-15s %-16s\n",
                    group.name
            );
        }
    }

    //endregion

} // TxCategory
