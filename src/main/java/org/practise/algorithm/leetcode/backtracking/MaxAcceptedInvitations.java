package org.practise.algorithm.leetcode.backtracking;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 1820. Maximum Number of Accepted Invitations
 * Medium
 *
 * There are m boys and n girls in a class attending an upcoming party.
 * You are given an m x n integer matrix grid, where grid[i][j] equals 0 or 1. If grid[i][j] == 1, then that means the ith boy can invite the jth girl to the party. A boy can invite at most one girl, and a girl can accept at most one invitation from a boy.
 * Return the maximum possible number of accepted invitations.
 *
 * Example 1:
 * Input: grid = [[1,1,1],
 *                [1,0,1],
 *                [0,0,1]]
 * Output: 3
 * Explanation: The invitations are sent as follows:
 * - The 1st boy invites the 2nd girl.
 * - The 2nd boy invites the 1st girl.
 * - The 3rd boy invites the 3rd girl.
 * Example 2:
 *
 * Input: grid = [[1,0,1,0],
 *                [1,0,0,0],
 *                [0,0,1,0],
 *                [1,1,1,0]]
 * Output: 3
 * Explanation: The invitations are sent as follows:
 * -The 1st boy invites the 3rd girl.
 * -The 2nd boy invites the 1st girl.
 * -The 3rd boy invites no one.
 * -The 4th boy invites the 2nd girl.
 *
 * Constraints:
 * grid.length == m
 * grid[i].length == n
 * 1 <= m, n <= 200
 * grid[i][j] is either 0 or 1.
 */
public class MaxAcceptedInvitations {
    public int maximumInvitations(int[][] grid) {
        int invitation = 0;
        int m = grid.length, n = grid[0].length;

        int[] girlFixed = new int[n]; // track boy fixed with the girl

        Arrays.fill(girlFixed, -1);

        for (int i = 0; i < m; i++) {
            if (dfs(grid, i, new HashSet<>() /* track invitation accepted girl */, girlFixed)){
                invitation++;
            }
        }
        return invitation;
    }

    private boolean dfs(int[][] grid, int boy, Set<Integer> seen, int[] girlFixed){
        int n = grid[0].length;

        for (int i = 0; i < n; i++) {
            if (grid[boy][i] == 1 && !seen.contains(i)){
                seen.add(i);

                if (girlFixed[i] == -1 || dfs(grid, girlFixed[i], seen, girlFixed)){
                    girlFixed[i] = boy;
                    return true;
                }
            }
        }
        return false;
    }
}
