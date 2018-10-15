package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NextPermutationTest {
    private NextPermutation obj = new NextPermutation();

    @Test
    public void testNextPermutation() {
        final int[] nums = {1, 2, 3};
        obj.nextPermutation(nums);
        Assert.assertEquals(nums, new int[]{1, 3, 2});
    }

    @Test
    public void testNextPermutation2() {
        final int[] nums = {1, 5, 3, 2};
        obj.nextPermutation(nums);
        Assert.assertEquals(nums, new int[]{2, 1, 3, 5});
    }

    @Test
    public void testNextPermutation3() {
        final int[] nums = {4, 3, 2, 1};
        obj.nextPermutation(nums);
        Assert.assertEquals(nums, new int[]{1, 2, 3, 4});
    }

    @Test
    public void testNextPermutation4() {
        final int[] nums = {2, 3, 1};
        obj.nextPermutation(nums);
        Assert.assertEquals(nums, new int[]{3, 1, 2});
    }
}