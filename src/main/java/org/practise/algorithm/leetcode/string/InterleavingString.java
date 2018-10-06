package org.practise.algorithm.leetcode.string;

/**
 Given s1, s2, s3, find whether s3 is formed by the interleaving of s1 and s2.

 Example 1:

 Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
 Output: true
 Example 2:

 Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
 Output: false
 */
public class InterleavingString {
    public boolean isInterleave(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) {
            return false;
        }
        if (s1 == null || s1.isEmpty()) {
            if (s2 != null) {
                if (s2.equals(s3)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        if (s2 == null || s2.isEmpty()) {
            if (s1 != null) {
                if (s1.equals(s3)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if ( (s1 == null && s2 == null && s3 == null) ||
                (s1.isEmpty() && s2.isEmpty() && s3.isEmpty())) {
            return true;
        }
        boolean dp[] = new boolean[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0 && j == 0) {
                    dp[j] = true;
                } else if (i == 0) {
                    dp[j] = dp[j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1);
                } else if (j == 0) {
                    dp[j] = dp[j] && s1.charAt(i - 1) == s3.charAt(i + j - 1);
                } else {
                    dp[j] = (dp[j] && s1.charAt(i - 1) == s3.charAt(i + j - 1) ||
                            dp[j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1)
                    );
                }
            }
        }
        return dp[s2.length()];
    }
}
