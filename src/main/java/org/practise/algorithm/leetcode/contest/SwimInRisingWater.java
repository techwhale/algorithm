package org.practise.algorithm.leetcode.contest;

public class SwimInRisingWater {
    public int swimInWater(int[][] grid) {
        findMinimumPath(grid);
        return grid[0][0];
    }

    private void findMinimumPath(int[][] grid) {
        int N = grid.length;
        for (int i = N - 1; i >= 0; i--) {
            int[] minimumPathRightToLeft = new int[N];
            int[] minimumPathLeftToRight = new int[N];
            for (int j = N - 1; j >= 0; j--) {
                minimumPathRightToLeft[j] = grid[i][j];
                minimumPathLeftToRight[j] = grid[i][j];
            }

            for (int j = N - 1; j >= 0; j--) {
                if (i == N - 1 && j == N - 1) continue;
                int pathRightToLeft = Math.min(j + 1 == N ? Integer.MAX_VALUE: minimumPathRightToLeft[j + 1],
                        i + 1 == N ? Integer.MAX_VALUE: grid[i + 1][j]);

                minimumPathRightToLeft[j] = Math.max(minimumPathRightToLeft[j], pathRightToLeft);

                int pathLeftToRight = Math.min(N - 1 - j == 0 ? Integer.MAX_VALUE: minimumPathLeftToRight[N - 2 - j],
                        i + 1 == N ? Integer.MAX_VALUE: grid[i + 1][N - 1 - j]);
                minimumPathLeftToRight[N - 1 - j] = Math.max(minimumPathLeftToRight[N - 1 - j], pathLeftToRight);
            }

            for (int j = N - 1; j >= 0; j--) {
                grid[i][j] = Math.min(minimumPathRightToLeft[j], minimumPathLeftToRight[j]);
            }
        }
    }
}
