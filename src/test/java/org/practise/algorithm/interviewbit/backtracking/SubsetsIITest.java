package org.practise.algorithm.interviewbit.backtracking;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SubsetsIITest {
    SubsetsII obj = new SubsetsII();
    @Test
    public void testSubsets() {
        int[] subset = {1,2,3};
        obj.subsets(subset);
    }
}