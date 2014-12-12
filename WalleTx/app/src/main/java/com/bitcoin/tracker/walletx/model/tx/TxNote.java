package com.bitcoin.tracker.walletx.model.tx;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.bitcoin.tracker.walletx.model.tx.Tx;

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
public class TxNote extends Model {

    @Column(name = "Note")
    public String note;

    @Column(name = "Tx") // TxNote belongs to one Tx
    public Tx tx;

    public TxNote() {
        super();
    }

    public TxNote(Tx tx, String note) {
        super();
        this.tx = tx;
        this.note = note;
    }

    /*------------------*
     *  TxNote Queries  *
     *------------------*/





} // TxNote
