package org.practise.algorithm.leetcode.binarysearch.medium;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MinimumDayForMBouquetTest {
    MinimumDayForMBouquet obj = new MinimumDayForMBouquet();
    @Test
    public void testMinDays() {
        assertEquals(obj.minDays(new int[]{1,10,3,10,2}, 3, 1), 3);
        assertEquals(obj.minDays(new int[]{1,10,3,10,2}, 3, 2), -1);
        assertEquals(obj.minDays(new int[]{7,7,7,7,12,7,7}, 2, 3), 12);
    }
}