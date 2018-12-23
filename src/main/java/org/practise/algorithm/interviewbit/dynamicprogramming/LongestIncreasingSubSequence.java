package org.practise.algorithm.interviewbit.dynamicprogramming;

public class LongestIncreasingSubSequence {
    public int lengthOfLIS(int[] nums) {
        int[] increasingSubSequenceLength =  new int[nums.length];
        // assign with default values
        for (int i = 0; i < nums.length; i++) {
            increasingSubSequenceLength[i] = 1;
        }

        int maxSubSequence = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (nums[i] > nums[j]) {
                    increasingSubSequenceLength[i] = Math.max(increasingSubSequenceLength[i],  increasingSubSequenceLength[j] + 1);
                }
            }
            maxSubSequence = Math.max(maxSubSequence, increasingSubSequenceLength[i]);
        }
        return maxSubSequence;
    }
}
