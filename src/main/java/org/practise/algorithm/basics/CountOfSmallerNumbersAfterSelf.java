package org.practise.algorithm.basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * You are given an integer array nums and you have to return a new counts array.
 * The counts array has the property where counts[i] is the number of smaller elements to the right of nums[i].
 *
 * Example:
 *
 * Input: [5,2,6,1]
 * Output: [2,1,1,0]
 * Explanation:
 * To the right of 5 there are 2 smaller elements (2 and 1).
 * To the right of 2 there is only 1 smaller element (1).
 * To the right of 6 there is 1 smaller element (1).
 * To the right of 1 there is 0 smaller element.
 */
public class CountOfSmallerNumbersAfterSelf {
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> resultList = new ArrayList<>();
        int[] copy = Arrays.copyOf(nums, nums.length);
        int[] bits = new int[nums.length + 1];
        Arrays.sort(copy);

        for (int i = nums.length - 1; i >= 0; i--) {
            int smallerCount = search(bits, findBITIndex(copy, nums[i] - 1));
            resultList.add(smallerCount);
            update(bits, findBITIndex(copy, nums[i]));
        }
        Collections.reverse(resultList);

        return resultList;
    }

    private int findBITIndex(int[] arr, int elem) {
        int index = Arrays.binarySearch(arr, elem);
        if (index < 0)  {
            // probable insertion index equal to BIT index.
            // no need to increment
            index = -index - 1;
        } else {
            index++;
        }
        return index;
    }

    private void update(int[] bits, int index) {
        while (index < bits.length) {
            bits[index] += 1;
            index += (index & -index);
        }
    }

    private int search(int[] bits, int index) {
        int count = 0;
        while (index > 0) {
            count += bits[index];
            index -= (index & - index);
        }
        return count;
    }
}
