package org.practise.algorithm.leetcode.binarysearch.medium;

import java.util.Arrays;
import java.util.List;

public class NumberOfInterval {
    public int[] intervalOccurence(int[] values, int[] intervals) {
        int[] counts = new int[intervals.length - 1];

        for (int i = 0; i < counts.length; i++) {
            int start = intervals[i];
            int end = intervals[i + 1];

            int startIndex = Arrays.binarySearch(values, start);
            if (startIndex < 0) {
                startIndex = -(startIndex + 1);
            }

            int endIndex = Arrays.binarySearch(values, end);
            if (endIndex < 0) {
                endIndex = -(endIndex + 1);
            }

            counts[i] = endIndex - startIndex;
        }
        return counts;
    }
}
