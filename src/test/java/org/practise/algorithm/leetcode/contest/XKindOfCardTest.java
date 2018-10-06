package org.practise.algorithm.leetcode.contest;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class XKindOfCardTest {
    XKindOfCard obj = new XKindOfCard();

    @Test
    public void testXkindOfCard() {
        int[] cards = {1,1,2,2,2,2};
        boolean condition = obj.hasGroupsSizeX(cards);
        Assert.assertEquals(condition, true);
    }

}