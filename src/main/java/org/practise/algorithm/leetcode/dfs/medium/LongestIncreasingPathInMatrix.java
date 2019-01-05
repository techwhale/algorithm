package org.practise.algorithm.leetcode.dfs.medium;

/**
 * Given an integer matrix, find the length of the longest increasing path.
 *
 * From each cell, you can either move to four directions: left, right, up or down. You may NOT move diagonally or move outside of the boundary
 * (i.e. wrap-around is not allowed).
 *
 * Example 1:
 *
 * Input: nums =
 * [
 *   [9,9,4],
 *   [6,6,8],
 *   [2,1,1]
 * ]
 * Output: 4
 * Explanation: The longest increasing path is [1, 2, 6, 9].
 * Example 2:
 *
 * Input: nums =
 * [
 *   [3,4,5],
 *   [3,2,6],
 *   [2,2,1]
 * ]
 * Output: 4
 * Explanation: The longest increasing path is [3, 4, 5, 6]. Moving diagonally is not allowed.
 */
public class LongestIncreasingPathInMatrix {
    private final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    public int longestIncreasingPath(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) return 0;

        int M = matrix.length, N = matrix[0].length;

        boolean[][] visited = new boolean[M][N];
        int[][] cache = new int[M][N];
        int maxPath = 1;
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                maxPath = Math.max(maxPath, findMaxPath(matrix, row, column, Integer.MIN_VALUE, visited, cache));
            }
        }

        return maxPath;
    }

    private int findMaxPath(int[][] matrix, int row, int column, int previousValue, boolean[][] visited, int[][] cache) {
        if (row < 0 || column < 0 || row >= matrix.length || column >= matrix[0].length || visited[row][column] || matrix[row][column] <= previousValue) {
            return 0;
        }
        // check for cached result
        if (cache[row][column] != 0) return cache[row][column];

        // avoid looking same row and column
        visited[row][column] = true;
        int maxPath = 0;
        for (int[] direction : DIRECTIONS) {
            maxPath = Math.max(maxPath, findMaxPath(matrix, row + direction[0], column + direction[1], matrix[row][column], visited, cache));
        }
        visited[row][column] = false;

        // update cache with max path found including current position (by adding 1)
        cache[row][column] = 1 + maxPath;
        return cache[row][column];
    }
}
