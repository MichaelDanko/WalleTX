package com.bitcoin.tracker.walletx.UnitTest;

import android.test.suitebuilder.annotation.SmallTest;

import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.QueryModelTest;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.Walletx;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by DanielCarroll on 3/22/2015.
 */
public class ModelTest extends TestCase {

    //will throw exception if setup incorrectly
    @Override
    public void setUp() throws Exception{
        super.setUp();
    }


    //run small test against WalletGroup Query model
    @SmallTest
    public void balanceQueryTest() throws ParseException {


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
