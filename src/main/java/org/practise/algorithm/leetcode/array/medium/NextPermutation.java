package org.practise.algorithm.leetcode.array.medium;


/**
 31. Next Permutation

 Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.

 If such arrangement is not possible, it must rearrange it as the lowest possible order (ie, sorted in ascending order).

 The replacement must be in-place and use only constant extra memory.

 Here are some examples. Inputs are in the left-hand column and its corresponding outputs are in the right-hand column.

 1,2,3 → 1,3,2
 3,2,1 → 1,2,3
 1,1,5 → 1,5,1
 */
public class NextPermutation {
    public void nextPermutation(int[] nums) {
        int N = nums.length;
        int swapPosition = findSwappablePosition(nums);
        if (swapPosition >= 0) {
            swap(nums, swapPosition);
        }
        sort(nums, swapPosition + 1);
    }

    private int findSwappablePosition(int[] nums) {
        int i = nums.length - 2;
        for (; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) return i;
        }
        return i;
    }

    private void swap(int[] nums, int swapPosition) {
        for (int i = nums.length - 1; i > 0; i--) {
            if (nums[swapPosition] < nums[i]){
                swap(nums, swapPosition, i);
                break;
            }
        }
    }

    private void swap(int[] nums, int position1, int position2) {
        int temp = nums[position1];
        nums[position1] = nums[position2];
        nums[position2] = temp;
    }

    private void sort(int nums[], int startPosition) {
        int endPosition = nums.length - 1;
        while (startPosition < endPosition) {
            swap(nums, startPosition, endPosition);
            startPosition++;
            endPosition--;
        }
    }
}
