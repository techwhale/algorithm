package org.practise.algorithm.leetcode.regular;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class InvalidTransactionTest {
    InvalidTransaction obj = new InvalidTransaction();
    @Test
    public void testInvalidTransactions() {
        String[] transactions = {"alice,20,800,mtv", "alice,50,100,beijing"};
        Assert.assertEquals(obj.invalidTransactions(transactions), Arrays.asList("alice,20,800,mtv","alice,50,100,beijing"));
    }
}