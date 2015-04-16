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
public class TxCategory extends Model {

    @Column(name = "Name")
    public String name = "Uncategorized";

    // Has many Tx's
    public List<Tx> txs() {
        return getMany(Tx.class, "TxCategory");
    }

    public TxCategory() {
        super();
    }

    public TxCategory(String name) {
        super();
        this.name = new String(name);
    }

    /*----------------------*
     *  TxCategory Queries  *
     *----------------------*/


    public static List<TxCategory> getAllCatagories(){
        return new Select()
                .from(TxCategory.class)
                .orderBy("Name ASC")
                .execute();
    }


    public static TxCategory getCatagoryName(String _name){
        return new Select()
                .from(TxCategory.class)
                .where("Name = ?", _name)
                .executeSingle();
    }




} // TxCategory
