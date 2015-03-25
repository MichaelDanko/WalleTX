package com.bitcoin.tracker.walletx.model;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DanielCarroll on 3/22/2015.
 */
public class QueryModelTest {


    /////////////////////////////////////////////////////////////////////////////////
    //builds fake db for groups
    ////////////////////////////////////////////////////////////////////////////////
    public void addGroupsTestDB(){

        //fake group names
        List<String> groupNames = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6");

        //puts groups into DB
        for (int j = 0; j < 6; j++){
            WalletGroup newGroup = new WalletGroup();
            newGroup.name = groupNames.get(j);
            newGroup.setAsDefault(0);

            WalletGroup last = WalletGroup.getLast();
            newGroup.displayOrder = last.displayOrder + 1;
            newGroup.save();
        }




       List<WalletGroup> query2 = WalletGroup.getAllSortedByName();
        for(WalletGroup group : query2){
            System.out.printf(
                    "%-20s %-15s %-16s\n",
                    group.name,
                    group.getDefaultGroup(),
                    group.displayOrder);
        }








    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //builds TX
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void addWTXTestDB(){

        List<String> WTXNames = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6");
        List<String> groupNames = Arrays.asList("group1", "group2", "group3", "group4", "group5", "group6");

        for (int j = 0; j < 6; j++){
            //add new tx category
            Walletx newWTX = new Walletx();
            newWTX.name = WTXNames.get(j);
            newWTX.type = WalletType.SINGLE_ADDRESS_WALLET;

            //adds new group for each tx
            WalletGroup newGroup = new WalletGroup();
            newGroup.name = groupNames.get(j);
            newGroup.setAsDefault(0);

            WalletGroup last = WalletGroup.getLast();
            newGroup.displayOrder = last.displayOrder + 1;
            newGroup.save();

            //adds group to tx and saves
            newWTX.group = newGroup;
            newWTX.save();

        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    //builds  balances
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public void addBalanceTestDB() throws ParseException {
        float[] balances = new float[5];

        String dt = "2008-01-01";  // Start date
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy");

        Calendar c =  Calendar.getInstance();
        c.setTime(sf.parse(dt));



        for(int j = 0; j < 6; j++) {
            Balance newBalance = new Balance();
            newBalance.balance = balances[j];
            newBalance.setDateFromString(dt);

            c.add(Calendar.DATE, 1);  // number of days to add
            dt = sf.format(c.getTime());  // dt is now the new dat

        }

    }





}
