package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BagOfTokensTest {
    private BagOfTokens obj = new BagOfTokens();

    @Test
    public void testBagOfTokens() {
        int[] tokens = new int[] {100,200,300,400};
        final int score = obj.bagOfTokensScore(tokens, 200);
        Assert.assertEquals(score, 2);
    }
}