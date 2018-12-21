package org.practise.algorithm.leetcode.array.medium;

import java.util.PriorityQueue;

/**
 * Given a n x n matrix where each of the rows and columns are sorted in ascending order, find the kth smallest element in the matrix.
 *
 * Note that it is the kth smallest element in the sorted order, not the kth distinct element.
 *
 * Example:
 *
 * matrix = [
 *    [ 1,  5,  9],
 *    [10, 11, 13],
 *    [12, 13, 15]
 * ],
 * k = 8,
 *
 * return 13.
 * Note:
 * You may assume k is always valid, 1 ≤ k ≤ n2.
 */
public class KthSmallestSortedMatrix {
    public int kthSmallest(int[][] matrix, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {return a[2] - b[2];});
        int N = matrix.length;
        for (int col = 0; col < matrix[0].length; col++) {
            pq.offer(new int[] {0, col, matrix[0][col]});
        }

        for (int i = 0; i < k - 1; i++) {
            int[] elem = pq.poll();
            if (elem[0] == N - 1) continue;
            int row = elem[0] + 1, col = elem[1];
            pq.offer(new int[] {row, col, matrix[row][col]});
        }
        return pq.poll()[2];
    }
}
