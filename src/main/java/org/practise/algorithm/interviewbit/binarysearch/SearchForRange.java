package org.practise.algorithm.interviewbit.binarysearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a sorted array of integers, find the starting and ending position of a given target value.
 *
 * Your algorithm’s runtime complexity must be in the order of O(log n).
 *
 * If the target is not found in the array, return [-1, -1].
 *
 * Example:
 *
 * Given [5, 7, 7, 8, 8, 10]
 *
 * and target value 8,
 *
 * return [3, 4].
 */
public class SearchForRange {
    public ArrayList<Integer> searchRange(final List<Integer> a, int target) {
        ArrayList<Integer> resultList = new ArrayList<>();
        final int firstOccurence = getFirstOccurence(a, target);
        if (firstOccurence == -1) return new ArrayList<>(Arrays.asList(-1, -1));
        resultList.add(firstOccurence);
        resultList.add(getLastOccurence(a, target));
        return resultList;
    }

    private int getFirstOccurence(final List<Integer> a, int target) {
        int low = 0, high = a.size() - 1;
        while (low <= high) {
            int mid = low + (high - low)/2;
            if ((mid == 0 || a.get(mid - 1) < target) && a.get(mid) == target) {
                return mid;
            } else if (a.get(mid) < target) {
                low = mid + 1;
            } else  {
                high = mid - 1;
            }
        }
        return -1;
    }


    private int getLastOccurence(final List<Integer> a, int target) {
        int low = 0, high = a.size() - 1;
        while (low <= high) {
            int mid = low + (high - low)/2;
            if ((mid == a.size() - 1 || a.get(mid + 1) > target) && a.get(mid) == target) {
                return mid;
            } else if (a.get(mid) > target) {
                high = mid - 1;
            } else  {
                low = mid + 1;
            }
        }
        return -1;
    }

}
