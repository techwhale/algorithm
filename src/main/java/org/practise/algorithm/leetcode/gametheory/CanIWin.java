package org.practise.algorithm.leetcode.gametheory;

public class CanIWin {
    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        if (maxChoosableInteger * (maxChoosableInteger + 1)/ 2 < desiredTotal)
            return false;
        if (desiredTotal <= 0) {
            return true;
        }
        int[] dp = new int[ 1 << maxChoosableInteger];
        return recursion(dp, 0, maxChoosableInteger, desiredTotal);
    }

    private boolean recursion(int[] dp, int mask, int max, int target) {
        if (target <= 0) { // previous person won
            return false;
        }
        if (dp[mask] != 0) {
            return dp[mask] == 1;
        }
        boolean win = false;
        for (int i = 0; i < max; i++) {
            if ( (mask & (1 << i)) == 0) {
                win = win || !  recursion(dp, mask | (1 << i), max, target - i - 1);
            }
        }
        dp[mask] = win ? 1 : -1;
        return win;
    }
}
