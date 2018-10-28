package org.practise.algorithm.leetcode.binarysearch.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class KokoEatingBananasTest {
    private KokoEatingBananas obj = new KokoEatingBananas();

    @Test
    public void testKokoEatingBananas() {
        final int[] piles = {3, 6, 7, 11};
        final int minEatingSpeed = obj.minEatingSpeed(piles, 8);
        Assert.assertEquals(minEatingSpeed, 4);
    }

    @Test
    public void testKokoEatingBananas2() {
        final int[] piles = {30,11,23,4,20};
        final int minEatingSpeed = obj.minEatingSpeed(piles, 5);
        Assert.assertEquals(minEatingSpeed, 30);
    }

    @Test
    public void testKokoEatingBananas3() {
        final int[] piles = {30,11,23,4,20};
        final int minEatingSpeed = obj.minEatingSpeed(piles, 6);
        Assert.assertEquals(minEatingSpeed, 23);
    }
}