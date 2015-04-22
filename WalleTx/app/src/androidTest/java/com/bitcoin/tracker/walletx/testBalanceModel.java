package com.bitcoin.tracker.walletx;

import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.QueryModelTest;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.text.ParseException;

/**
 * Created by DanielCarroll on 3/29/2015.
 */
public class testBalanceModel extends ApplicationTest {


/*
Need to figure out how to pass an existing WalleTx modeol to getBalance query to test otherwise see
QueryModelTest for dump and proper data building.
 */
   public void testBalance () throws ParseException{

       QueryModelTest newTest = new QueryModelTest();

       newTest.addBalanceTestDB();



       //not sure that will work
       Balance query = Balance.getBalance(Walletx.getBy("test1"));
       assertEquals(query, 1.00);



   }
}
