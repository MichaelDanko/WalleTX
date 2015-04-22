package com.bitcoin.tracker.walletx;

import com.bitcoin.tracker.walletx.model.ExchangeRate;
import com.bitcoin.tracker.walletx.model.QueryModelTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.prefs.AbstractPreferences;

/**
 * Created by DanielCarroll on 3/29/2015.
 */
public class testExchangeRateModel extends ApplicationTest {

    public void testExchangeRate () throws ParseException{
        String dt = "2008-01-01";  // Start date

        QueryModelTest newTest = new QueryModelTest();
        ExchangeRate testExchange = new ExchangeRate();
        testExchange.setDateFromString(dt);
        testExchange.usd = 1f;
        testExchange.eur = 7f;
        testExchange.gbp = 13f;
        testExchange.save();

        newTest.addExchangeRateDB();



        ExchangeRate query1 = ExchangeRate.fromDateEUR(dt);
        assertEquals(query1, testExchange);
    }
}
