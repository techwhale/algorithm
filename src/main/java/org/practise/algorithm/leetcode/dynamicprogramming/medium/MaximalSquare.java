package org.practise.algorithm.leetcode.dynamicprogramming.medium;

public class MaximalSquare {
    public int maximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return 0;
        }
        int row = matrix.length, col = row > 0 ?  matrix[0].length : 0;
        int[] dp = new int[col + 1];
        int squarelength = 0, prev = 0;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                int temp = dp[j];
                if (matrix[i - 1][j - 1] == '1') {
                    dp[j] = Math.min(Math.min(dp[j], dp[j -1]), prev) + 1;
                    squarelength = Math.max(squarelength, dp[j]);
                } else {
                    dp[j] = 0;
                }
                prev = temp;
            }
        }
        return squarelength * squarelength;
    }
}
