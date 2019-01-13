package org.practise.algorithm.basics.dynamicprogramming;

public class LongestCommonSubSequence {
    public int findLongestCommonSubSequence(String s, String t) {
        int sLength = s.length(), tLength = t.length();
        int[][] dp = new int[sLength + 1][tLength + 1];

        for (int i = 0; i <= sLength; i++) {
            for (int j = 0; j <= tLength; j++) {
                if (i == 0  || j == 0)
                    continue;
                char s_char = s.charAt(i - 1), t_char = t.charAt(j - 1);
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (s_char == t_char) {
                    dp[i][j] = Math.max(dp[i][j], 1 + dp[i - 1][j - 1]);
                }
            }
        }
        return dp[sLength][tLength];
    }
}
