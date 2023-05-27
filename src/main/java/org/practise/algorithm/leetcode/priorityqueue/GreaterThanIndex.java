package org.practise.algorithm.leetcode.priorityqueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Input a = [21,5,6,56,88,52], output = [5,5,5,4,-1,-1] .
 * Output array values is made up of indices of the element with value greater than the current element but with largest index.
 * So 21 < 56 (index 3), 21 < 88 (index 4) but also 21 < 52 (index 5) so we choose index 5 (value 52).
 * Same applies for 5,6 and for 56 its 88 (index 4).
 * If there is no greater element then use -1 and last element of the array will always have value of -1 in output array since there is no other elment after it.
 * Follow up to consider the input as a stream, how can we only update smaller element (use specific Data structure), running time and space complexity discussion.
 */
public class GreaterThanIndex {
    public int[] greaterThanIndex(int[] arr) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new CustomComparator());
        for (int i = 0; i < arr.length; i++) {
            pq.offer(new int[] {arr[i], i});
        }
        int[] result = new int[arr.length];
        Arrays.fill(result, -1);

        int maxIdx = -1;
        while (!pq.isEmpty()) {
            int[] val = pq.poll();

            if (maxIdx == -1) {
                maxIdx = val[1];
                continue;
            } else {
                if (val[1] > maxIdx) {
                    maxIdx = val[1];
                } else {
                    result[val[1]] = maxIdx;
                }
            }

        }
        return result;
    }

    class CustomComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            if (a[0] != b[0]) {
                return b[0] - a[0];
            } else {
                return b[1] - a[1];
            }
        }
    }
}
