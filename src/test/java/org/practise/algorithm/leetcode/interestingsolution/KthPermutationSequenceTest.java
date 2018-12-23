package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.interestingideas.KthPermutationSequence;
import org.testng.Assert;
import org.testng.annotations.Test;

public class KthPermutationSequenceTest {
    private KthPermutationSequence obj = new KthPermutationSequence();

    @Test
    public void testKthPermutationSequence() {
        final String permutation = obj.getPermutation(11, 1);
        Assert.assertEquals(permutation, "1234567891011");
    }
}