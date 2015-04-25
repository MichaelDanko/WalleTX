package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * TxNote model.
 *
 * TxNote is simply a note that can be applied to
 * individual transactions.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "TxNote")
public class Note extends Model {

    @Column(name = "Note")
    public String note;

    @Column(name = "Tx") // TxNote belongs to one Tx
    public Tx tx;

    public Note() {
        super();
    }

    public Note(Tx tx, String note) {
        super();
        this.tx = tx;
        this.note = note;
    }

    /*------------------*
     *  TxNote Queries  *
     *------------------*/


    /**
     *
     * @return list of all notes
     */
    public static List<Note> getAll(){
        return new Select()
                .from(Note.class)
                .execute();
    }

    /**
     *
     * @param tx
     * @return note related to blockchainInfoWalletData
     */

    public static Note getBy(Tx tx){
        return new Select()
                .from(Note.class)
                .where("tx = ?", tx)
                .executeSingle();
    }


} // TxNote
