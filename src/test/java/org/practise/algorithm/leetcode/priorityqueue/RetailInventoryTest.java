package org.practise.algorithm.leetcode.priorityqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RetailInventoryTest {
    private RetailInventory inventory = new RetailInventory();
    @Test
    public void testFindTotalWeight() {
        Assert.assertEquals(inventory.findTotalWeight(new int[]{6,4,9,10,34,56,54}), 68);
    }
}