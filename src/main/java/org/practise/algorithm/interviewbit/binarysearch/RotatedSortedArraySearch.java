package org.practise.algorithm.interviewbit.binarysearch;

import java.util.List;

/**
 * Suppose a sorted array is rotated at some pivot unknown to you beforehand.
 *
 * (i.e., 0 1 2 4 5 6 7  might become 4 5 6 7 0 1 2 ).
 *
 * You are given a target value to search. If found in the array, return its index, otherwise return -1.
 *
 * You may assume no duplicate exists in the array.
 *
 * Input : [4 5 6 7 0 1 2] and target = 4
 * Output : 0
 *
 * NOTE : Think about the case when there are duplicates. Does your current solution work? How does the time complexity change?*
 */
public class RotatedSortedArraySearch {
    // DO NOT MODIFY THE LIST
    public int search(final List<Integer> a, int target) {
        int low = 0, high = a.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (a.get(mid) == target) return mid;
            else if (a.get(low) <= a.get(mid)) { // check low to mid is sorted
                if (a.get(low) <= target &&  target <= a.get(mid)) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else { // mid to high should be sorted based on previous condition
                if (a.get(mid) <= target && target <= a.get(high)) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -1;
    }
}
