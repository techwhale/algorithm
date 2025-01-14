package org.practise.algorithm.leetcode.dynamicprogramming.easy;

/**
 * 276. Paint Fence
 *
 * There is a fence with n posts, each post can be painted with one of the k colors.
 *
 * You have to paint all the posts such that no more than two adjacent fence posts have the same color.
 *
 * Return the total number of ways you can paint the fence.
 *
 * Note:
 * n and k are non-negative integers.
 *
 * Example:
 *
 * Input: n = 3, k = 2
 * Output: 6
 * Explanation: Take c1 as color 1, c2 as color 2. All possible ways are:
 *
 *             post1  post2  post3
 *  -----      -----  -----  -----
 *    1         c1     c1     c2
 *    2         c1     c2     c1
 *    3         c1     c2     c2
 *    4         c2     c1     c1
 *    5         c2     c1     c2
 *    6         c2     c2     c1
 */
public class PaintFence {
    public int numWays(int n, int k) {
        if (n == 0) return 0;
        int sameColor = 0, differentColor = k;
        for (int i = 1; i < n; i++) {
            int temp = differentColor;
            differentColor = (differentColor + sameColor) * (k - 1);
            sameColor = temp;
        }
        return sameColor + differentColor;
    }


//    Map<Integer, Integer> map = new HashMap<>();
//    public int numWays(int n, int k) {
//        if (n == 1) return k;
//        if (n == 2) return k * k;
//
//        if (map.containsKey(n)) {
//            return map.get(n);
//        }
//        map.put(n, (k - 1) * (numWays(n - 1, k) + numWays(n - 2, k)));
//        return map.get(n);
//    }
}
