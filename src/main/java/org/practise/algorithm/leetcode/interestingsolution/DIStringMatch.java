package org.practise.algorithm.leetcode.interestingsolution;

/**
 * Given a string S that only contains "I" (increase) or "D" (decrease), let cols = S.length.
 *
 * Return any permutation A of [0, 1, ..., cols] such that for all i = 0, ..., cols-1:
 *
 * If S[i] == "I", then A[i] < A[i+1]
 * If S[i] == "D", then A[i] > A[i+1]
 *
 *
 * Example 1:
 *
 * Input: "IDID"
 * Output: [0,4,1,3,2]
 * Example 2:
 *
 * Input: "III"
 * Output: [0,1,2,3]
 * Example 3:
 *
 * Input: "DDI"
 * Output: [3,2,0,1]
 *
 *
 * Note:
 *
 * 1 <= S.length <= 10000
 * S only contains characters "I" or "D".
 */
public class DIStringMatch {
    public int[] diStringMatch(String S) {
        int N = S.length();
        int lo = 0, hi = N;
        int[] ans = new int[N + 1];
        for (int i = 0; i < N; ++i) {
            if (S.charAt(i) == 'I')
                ans[i] = lo++;
            else
                ans[i] = hi--;
        }

        ans[N] = lo;
        return ans;
    }
}
