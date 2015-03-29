package com.bitcoin.tracker.walletx;

import android.test.suitebuilder.annotation.SmallTest;

import com.bitcoin.tracker.walletx.model.QueryModelTest;
import com.bitcoin.tracker.walletx.model.WalletGroup;

import java.text.ParseException;

/**
 * Created by DanielCarroll on 3/22/2015.
 */

/*
This test will test building part of the database (specifically the WalletGroup) then test the
results of the queries in the WalletGroup Model
 */
public class testWalletGroupModel extends ApplicationTest {

    //will throw exception if setup incorrectly
    @Override
    public void setUp() throws Exception{
        super.setUp();
    }


    //run small test against WalletGroup Query model
    @SmallTest
    public void testWalletGroup() throws ParseException {


        QueryModelTest newTest = new QueryModelTest();

        newTest.addGroupsTestDB();

        String query1 = WalletGroup.getBy("test3").name;
        assertEquals(query1, "test3");

        query1 = WalletGroup.getDefault().name;
        assertEquals(query1, "My Wallets");

        query1 = WalletGroup.getLast().name;
        assertEquals(query1, "test6");

        query1 = WalletGroup.getByDisplayOrder(3).name;
        assertEquals(query1, "test2");

    }

    //will throw exception if tear down is incorrect
    @Override
    protected void tearDown() throws Exception{
        super.tearDown();
    }

}
