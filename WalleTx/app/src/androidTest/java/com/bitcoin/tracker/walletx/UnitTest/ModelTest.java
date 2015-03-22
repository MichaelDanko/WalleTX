package com.bitcoin.tracker.walletx.UnitTest;

import com.bitcoin.tracker.walletx.model.Walletx;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by DanielCarroll on 3/22/2015.
 */
public class ModelTest extends TestCase {

   public void tryTest(){
       Walletx wtx = new Walletx();
       wtx.getAll();
       wtx.dump();
   }
}
