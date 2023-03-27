package org.practise.algorithm.leetcode.dynamicprogramming.easy;

import java.util.*;

/**
 * 70. Climbing Stairs
 *
 * You are climbing a stair case. It takes n steps to reach to the top.
 *
 * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
 *
 * Note: Given n will be a positive integer.
 *
 * Example 1:
 *
 * Input: 2
 * Output: 2
 * Explanation: There are two ways to climb to the top.
 * 1. 1 step + 1 step
 * 2. 2 steps
 * Example 2:
 *
 * Input: 3
 * Output: 3
 * Explanation: There are three ways to climb to the top.
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 */
public class ClimbingStairs {
    public int climbStairs(int n) {
        // can be further optimized by using first & second
        int[] mem = new int[n + 1];
        mem[0] = mem[1] = 1;
        for (int i = 2; i <= n; i++) {
            mem[i] = mem[i - 1] + mem[i - 2];
        }
        return mem[n];
    }
//    un optimized approach
//    public int climbStairs(int n) {
//        return climb_Stairs(0, n);
//    }
//    public int climb_Stairs(int i, int n) {
//        if (i > n) {
//            return 0;
//        }
//        if (i == n) {
//            return 1;
//        }
//        return climb_Stairs(i + 1, n) + climb_Stairs(i + 2, n);
//    }
}