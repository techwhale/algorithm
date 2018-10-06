package org.practise.algorithm.leetcode.array.medium;

import java.util.Deque;
import java.util.LinkedList;

/**
 Return the length of the shortest, non-empty, contiguous subarray of A with sum at least K.

 If there is no non-empty subarray with sum at least K, return -1.



 Example 1:

 Input: A = [1], K = 1
 Output: 1
 Example 2:

 Input: A = [1,2], K = 4
 Output: -1
 Example 3:

 Input: A = [2,-1,2], K = 3
 Output: 3


 Note:

 1 <= A.length <= 50000
 -10 ^ 5 <= A[i] <= 10 ^ 5
 1 <= K <= 10 ^ 9
 */
public class ShortestSubarraySumatLeastK {
    public int shortestSubarray(int[] A, int K) {
        if (A == null || A.length == 0) {
            return -1;
        }
        int N = A.length;
        int[] P = new int[N + 1];
        for (int i = 0; i < N; i++)
            P[i + 1] = P[i] + A[i];

        int result = N + 1;
        Deque<Integer> queue = new LinkedList<>();
        for (int i = 0; i < P.length; i++) {
            while (!queue.isEmpty() && P[i] <= P[queue.getLast()])
                queue.removeLast();
            while (! queue.isEmpty() && P[i] - P[queue.getFirst()] >= K)
                result = Math.min(result, i - queue.removeFirst());

            queue.add(i);
        }
        return result == N + 1 ? -1 : result;
    }
}
