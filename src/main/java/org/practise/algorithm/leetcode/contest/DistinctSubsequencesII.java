package org.practise.algorithm.leetcode.contest;

import java.util.Arrays;

/**
 * Given a string S, count the number of distinct, non-empty subsequences of S .
 *
 * Since the result may be large, return the answer modulo 10^9 + 7.
 *
 *
 *
 * Example 1:
 *
 * Input: "abc"
 * Output: 7
 * Explanation: The 7 distinct subsequences are "a", "b", "c", "ab", "ac", "bc", and "abc".
 * Example 2:
 *
 * Input: "aba"
 * Output: 6
 * Explanation: The 6 distinct subsequences are "a", "b", "ab", "ba", "aa" and "aba".
 * Example 3:
 *
 * Input: "aaa"
 * Output: 3
 * Explanation: The 3 distinct subsequences are "a", "aa" and "aaa".
 *
 *
 *
 *
 * Note:
 *
 * S contains only lowercase letters.
 * 1 <= S.length <= 2000
 *
 */
public class DistinctSubsequencesII {
    public int distinctSubseqII(String S) {
        return distinctSubsequence(S.toCharArray(), 1000000007);
    }

    public int distinctSubsequence(char[] a, int mod)
    {
        int n = a.length;
        int[] bucket = new int[26];
        Arrays.fill(bucket, -1);

        int cum = 0;
        for(int i = 0;i < n;i++){
            int v = cum;
            int ind = a[i]-'a';
            if(bucket[ind] == -1){
                v++;
            }else{
                v += mod - bucket[ind];
            }
            if(v >= mod)v -= mod;
            bucket[ind] = cum;
            cum += v;
            if(cum >= mod)cum -= mod;
        }
        return cum;
    }
}
