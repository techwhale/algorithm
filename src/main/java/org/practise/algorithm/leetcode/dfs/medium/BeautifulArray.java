package org.practise.algorithm.leetcode.dfs.medium;

/**
 * For some fixed N, an array A is beautiful if it is a permutation of the integers 1, 2, ..., N, such that:
 *
 * For every i < j, there is no k with i < k < j such that A[k] * 2 = A[i] + A[j].
 *
 * Given N, return any beautiful array A.  (It is guaranteed that one exists.)
 *
 *
 *
 * Example 1:
 *
 * Input: 4
 * Output: [2,1,4,3]
 * Example 2:
 *
 * Input: 5
 * Output: [3,1,2,5,4]
 *
 *
 * Note:
 *
 * 1 <= N <= 1000
 */
public class BeautifulArray {
    public int[] beautifulArray(int N) {
        int[] arr = new int[N];
        dfs(1, 0, N, arr, 0);
        for (int i = 0; i < N; i++) arr[i]++;
        return arr;
    }
    private int dfs(int d, int m, int n, int[] arr, int offset) {
        if (m > n) return offset;
        if (m + d >= n) {
            arr[offset++] = m;
            return offset;
        }

        for (int i = 0; i < 2; i++) {
            offset = dfs(d * 2, m + d * i, n, arr, offset);
        }
        return offset;
    }
}
