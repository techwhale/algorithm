package org.practise.algorithm.leetcode.dynamicprogramming.easy;

public class MinimumFallingPath {
    public int minFallingPathSum(int[][] A) {
        if (A.length == 0) return 0;
        for (int i = 1 ; i < A.length; i++) {
            int M = A[0].length;
            for (int j = 0; j < M ; j++) {
                A[i][j] += Math.min(A[i - 1][j], Math.min((j == 0)? Integer.MAX_VALUE: A[i - 1][j - 1], (j == M - 1)? Integer.MAX_VALUE: A[i - 1][j + 1]));
            }
        }
        int min = Integer.MAX_VALUE, i = A.length - 1;
        for (int j = 0; j < A[0].length; j++) {
            min = Math.min(A[i][j], min);
        }
        return min;
    }
}
