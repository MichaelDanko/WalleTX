package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * TxNote table
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

    /*
     * TxNote Queries
     *
     */





} // TxNote
