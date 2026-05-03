package org.practise.algorithm.leetcode.dynamicprogramming.medium;

/**
 * 72. Edit Distance
 * Medium
 * Topics
 * Companies
 * Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.
 *
 * You have the following three operations permitted on a word:
 *
 * Insert a character
 * Delete a character
 * Replace a character
 *
 *
 * Example 1:
 *
 * Input: word1 = "horse", word2 = "ros"
 * Output: 3
 * Explanation:
 * horse -> rorse (replace 'h' with 'r')
 * rorse -> rose (remove 'r')
 * rose -> ros (remove 'e')
 * Example 2:
 *
 * Input: word1 = "intention", word2 = "execution"
 * Output: 5
 * Explanation:
 * intention -> inention (remove 't')
 * inention -> enention (replace 'i' with 'e')
 * enention -> exention (replace 'n' with 'x')
 * exention -> exection (replace 'n' with 'c')
 * exection -> execution (insert 'u')
 *
 *
 * Constraints:
 *
 * 0 <= word1.length, word2.length <= 500
 * word1 and word2 consist of lowercase English letters.
 */
public class EditDistance {

    public int minDistance2(String word1, String word2) {
        int M = word1.length();
        int N = word2.length();
        Integer[][] memo = new Integer[M + 1][N + 1];
        return minDistance(word1, word2, M, N, memo);
    }

    private int minDistance(String word1, String word2, int M, int N, Integer[][] memo) {
        if (memo[M][N] != null) {
            return memo[M][N];
        }
        if (M == 0) {
            return N;
        }
        if (N == 0) {
            return M;
        }
        int distance = 0;
        if (word1.charAt(M - 1) == word2.charAt(N - 1)) {
            distance = minDistance(word1, word2, M - 1, N -1, memo);
        } else {
            int insertOperation =
                    minDistance(word1, word2, M, N - 1, memo);
            int deleteOperation =
                    minDistance(word1, word2, M - 1, N, memo);
            int replaceOperation =
                    minDistance(word1, word2, M - 1, N - 1, memo);
            distance = Math.min(insertOperation, Math.min(deleteOperation, replaceOperation)) + 1;
        }
        memo[M][N] = distance;
        return distance;
    }


    public int minDistance(String word1, String word2) {
        int M = word1.length(), N = word2.length();
        if (M == 0) {
            return N;
        }
        if (N == 0) {
            return M;
        }
        int[][] memo = new int[M + 1][N + 1];

        for (int i = 1; i <= M; i++) {
            memo[i][0] = i;
        }
        for (int j = 1; j <= N; j++) {
            memo[0][j] = j;
        }

        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= N; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    memo[i][j] = memo[i - 1][j - 1];
                } else {
                    memo[i][j] = Math.min(memo[i - 1][j - 1], Math.min(
                            memo[i - 1][j], memo[i][ j -1]
                    )) + 1;
                }
            }
        }
        return memo[M][N];
    }
}
