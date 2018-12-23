package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.interestingideas.TopVotedCandidate;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TopVotedCandidateTest {
    @Test
    public void testTopVotedCandidate() {
        final int[] persons = {0, 1, 1, 0, 0, 1, 0};
        final int[] times = {0,5,10,15,20,25,30};
        TopVotedCandidate obj = new TopVotedCandidate(persons, times);
        Assert.assertEquals(obj.q(3), 0);
        Assert.assertEquals(obj.q(12), 1);
        Assert.assertEquals(obj.q(25), 1);
        Assert.assertEquals(obj.q(15), 0);
        Assert.assertEquals(obj.q(24), 0);
        Assert.assertEquals(obj.q(8), 1);
    }
}