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

    @Column(name = "Name")
    public String name = "Uncategorized";

  /*  @Column(name = "DefaultCategory")
    private int defaultCategory;

    public int getDefaultCategory(){
        return defaultCategory;
    }
*/
 /*   private void setAsDefault(boolean isDefault){
        if(isDefault){
            TxCategory currentDefault = TxCategory.getDefault();
            if(currentDefault != null){
                currentDefault.defaultCategory = 0;
                currentDefault.save();
            }
            this.defaultCategory = 1;
        }else this.defaultCategory = 0;
    }
*/
   // @Column(name = "DisplayOrder")
    //private int displayOrder;

    //public int getDisplayOrder() {
      //  return this.displayOrder;
    //}

    public List<Walletx>walletxs(){
        return getMany(Walletx.class,"WalletType");
    }

    public Category() {
        super();
    }

    public static void createTxCategory(String name, boolean isDefault) {
        Category newCat = new Category();
        newCat.name = name;

       // TxCategory last = TxCategory.getLast();
        //newCat.displayOrder = last.getDisplayOrder() + 1;
        // Set group display order and save.
        //newCat.setAsDefault(isDefault);
        newCat.save();
    }

  /*  public static void initDefaultGroup(Context context) {
        List<TxCategory> groups = TxCategory.getAll();
        if ( groups.size() < 1 ) {
            TxCategory defaultCategory = new TxCategory();
            defaultCategory.name = context.getResources().getString(R.string.wallet_group_wtx_default_group);
            defaultCategory.displayOrder = 1;
            defaultCategory.setAsDefault(true);
            defaultCategory.save();
        }
    }
*/
    // Has many Tx's
    public List<Tx> txs() {
        return getMany(Tx.class, "TxCategory");
    }



    public Category(String name) {
        super();
        this.name = new String(name);
    }

    /*----------------------*
     *  TxCategory Queries  *
     *----------------------*/


    public static Category getBy(String name) {
        return new Select()
                .from(Category.class)
                .where("Name = ?", name)
                .executeSingle();
    }


    public static List<Category> getAll() {
        return new Select()
                .from(Category.class)
                .execute();
    }

    /*public static List<TxCategory> getAllSortedByDisplayOrder() {
        return new Select()
                .from(TxCategory.class)
                .orderBy("DisplayOrder ASC")
                .execute();
    }*/

    public static List<Category> getAllSortedByName() {
        return new Select()
                .from(Category.class)
                .orderBy("Name ASC")
                .execute();
    }

   /* public static List<TxCategory> getAllWithDisplayOrderGreaterThan(int displayOrder) {
        return new Select()
                .from(TxCategory.class)
                .where("DisplayOrder > ?", displayOrder)
                .orderBy("DisplayOrder ASC")
                .execute();
    }*/

  /*  public static TxCategory getDefault() {
        return new Select()
                .from(TxCategory.class)
                .where("DefaultCategory = ?", 1)
                .executeSingle();
    }
*/
  /*  public boolean isDefault() {
        return this.defaultCategory == 1;
    }
*/
    /*public static TxCategory getLast() {
        return new Select()
                .from(TxCategory.class)
                .orderBy("DisplayOrder DESC")
                .executeSingle();
    }*/

   /* public static TxCategory getByDisplayOrder(int displayOrder) {
        return new Select()
                .from(TxCategory.class)
                .where("DisplayOrder = ?", displayOrder)
                .executeSingle();
    }*/

    public void update(String name, boolean isDefault) {
        this.name = name;
        //this.setAsDefault(isDefault);
        this.save();
    }

    public void updateName(String name) {
        this.name = name;
        this.save();
    }
/*
    public void updateDefault(boolean isDefault) {
        this.setAsDefault(isDefault);
        this.save();
    }
*/
public static void deleteCategory(Category delete) {


}

    public String toString(){
        return name;
    }

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
} // TxCategory
