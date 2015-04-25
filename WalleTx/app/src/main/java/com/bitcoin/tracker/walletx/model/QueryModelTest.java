package com.bitcoin.tracker.walletx.model;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DanielCarroll on 3/22/2015.
 */
public class QueryModelTest {
/*

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
            //newGroup.setAsDefault(0);

            WalletGroup last = WalletGroup.getLast();
            //newGroup.displayOrder = last.displayOrder + 1;
            newGroup.save();
        }




       List<WalletGroup> query2 = WalletGroup.getAllSortedByName();
        for(WalletGroup group : query2){
            System.out.printf(
                    "%-20s %-15s %-16s\n",
                    group.name,
                    group.getDefaultGroup()
                   // group.displayOrder
            );
        }


    }
*/
    /////////////////////////////////////////////////////////////////////////////////////////////
    //builds WalleTx
    //////////////////////////////////////////////////////////////////////////////////////////////
    public void addWTXTestDB(){

        List<String> WTXNames = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6");
        List<String> groupNames = Arrays.asList("group1", "group2", "group3", "group4", "group5", "group6");

        for (int j = 0; j < 6; j++){
            //add new tx category
            Walletx newWTX = new Walletx();
            newWTX.name = WTXNames.get(j);
            newWTX.type = SupportedWalletType.SINGLE_ADDRESS_WALLET;

            //adds new group for each tx
            Group newGroup = new Group();
            newGroup.name = groupNames.get(j);
            //newGroup.setAsDefault(0);

            Group last = Group.getLast();
            //newGroup.displayOrder = last.displayOrder + 1;
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
        float[] balances = new float[6];

        for (int j = 0; j < 6; j++){
            balances[j] += 1.00;
        }

        addWTXTestDB();

        String dt = "2008-01-01";  // Start date


        Balance newBalance = new Balance();
        for(int j = 0; j < 6; j++) {

            newBalance.balance = balances[j];
            newBalance.setDateFromString(dt);



        }

        newBalance.dump();

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //builds Exchange Rate
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public void addExchangeRateDB() throws ParseException {



        String dt = "2008-01-01";  // Start date



        List<Float> USDexchange = Arrays.asList(1f, 2f, 3f, 4f, 5f, 6f);
        List<Float> EURexchange = Arrays.asList(7f, 8f, 9f, 10f, 11f, 12f);
        List<Float> GBPexchange = Arrays.asList(13f, 14f, 15f, 16f, 17f, 18f);

        ExchangeRate newExchangeRate = new ExchangeRate();

        for (int j = 0; j < 6; j++){

            newExchangeRate.setDateFromString(dt);
            newExchangeRate.usd = USDexchange.get(j);
            newExchangeRate.eur = EURexchange.get(j);
            newExchangeRate.gbp = GBPexchange.get(j);
            newExchangeRate.save();



        }

    }




}
