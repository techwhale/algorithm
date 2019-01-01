package org.practise.algorithm.leetcode.combination;

import java.util.Arrays;

/**
 * Given a string S, count the number of distinct, non-empty subsequences of S .
 * <p>
 * Since the result may be large, return the answer modulo 10^9 + 7.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: "abc"
 * Output: 7
 * Explanation: The 7 distinct subsequences are "a", "b", "c", "ab", "ac", "bc", and "abc".
 * Example 2:
 * <p>
 * Input: "aba"
 * Output: 6
 * Explanation: The 6 distinct subsequences are "a", "b", "ab", "ba", "aa" and "aba".
 * Example 3:
 * <p>
 * Input: "aaa"
 * Output: 3
 * Explanation: The 3 distinct subsequences are "a", "aa" and "aaa".
 * <p>
 * <p>
 * <p>
 * <p>
 * Note:
 * <p>
 * S contains only lowercase letters.
 * 1 <= S.length <= 2000
 */
public class DistinctSubsequencesII {
    public int distinctSubseqII(String S) {
        final int N = S.length();
        int MOD = 1_000_000_007;
        int[] last = new int[26], dp = new int[N + 1];
        Arrays.fill(last, -1);
        dp[0] = 1;
        for (int i = 1; i <= N; i++) {
            int index = S.charAt(i - 1) - 'a';
            dp[i] = (2 * dp[i - 1]) % MOD;

            if (last[index] != -1) {
                dp[i] -= dp[last[index]];
            }

            dp[i] %= MOD;
            last[index] = i - 1;
        }

        dp[N]--;

        if (dp[N] < 0) {
            dp[N] += MOD;
        }

        return dp[N];
    }
}
