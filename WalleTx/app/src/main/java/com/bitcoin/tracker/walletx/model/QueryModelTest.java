package com.bitcoin.tracker.walletx.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DanielCarroll on 3/22/2015.
 */
public class QueryModelTest {


    /*
    Test for Model WalletGroup
     */
    public void addGroupsTestDB(){

        //adds to db
        List<String> groupNames = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6");


        for (int j = 0; j < 6; j++){
            WalletGroup newGroup = new WalletGroup();
            newGroup.name = groupNames.get(j);
            newGroup.setAsDefault(0);

            WalletGroup last = WalletGroup.getLast();
            newGroup.displayOrder = last.displayOrder + 1;
            newGroup.save();
        }

      String query1 = WalletGroup.getBy("test3").name;
       System.out.printf(query1);

       List<WalletGroup> query2 = WalletGroup.getAllSortedByName();
        for(WalletGroup group : query2){
            System.out.printf(
                    "%-20s %-15s %-16s\n",
                    group.name,
                    group.getDefaultGroup(),
                    group.displayOrder);
        }



       query1 = WalletGroup.getDefault().name;
      System.out.printf(query1);

       query1 = WalletGroup.getLast().name;
       System.out.printf(query1);

      query1 = WalletGroup.getByDisplayOrder(3).name;
       System.out.printf(query1);




    }

}
