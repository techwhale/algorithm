package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import java.util.ArrayList;
import java.util.List;

public class BurstBalloons {
    public int maxCoins(int[] iNums) {
        int[] nums = new int[iNums.length + 2];
        int n = 1;
        for (int x : iNums) if (x > 0) nums[n++] = x;
        nums[0] = nums[n++] = 1;


        int[][] dp = new int[n][n];
        for (int k = 2; k < n; ++k)
            for (int left = 0; left < n - k; ++left) {
                int right = left + k;
                for (int i = left + 1; i < right; ++i)
                    dp[left][right] = Math.max(dp[left][right],
                            nums[left] * nums[i] * nums[right] + dp[left][i] + dp[i][right]);
            }

        return dp[0][n - 1];
    }

//    public int maxCoins(int[] iNums) {
//        final int N = iNums.length;
//        List<Integer> nums = new ArrayList<>();
//        for (int num : iNums) {
//            nums.add(num);
//        }
//        return burstBalloons(nums);
//    }
//
//    private int burstBalloons(List<Integer> nums) {
//        int maxCoins = 0;
//        if (nums.size() == 1) {
//            return nums.get(0);
//        } else {
//            for (int i = 0; i < nums.size(); i++) {
//                int left = i == 0 ? 1 :  nums.get(i - 1);
//                int right = i ==  nums.size() -  1 ?  1 : nums.get(i + 1);
//
//                List<Integer> temp = new ArrayList<>(nums);
//                temp.remove(i);
//                maxCoins = Math.max(maxCoins , left * right * nums.get(i) + burstBalloons(temp));
//            }
//        }
//        return maxCoins;
//    }
}
