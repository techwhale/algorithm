package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import java.util.Comparator;
import java.util.PriorityQueue;

public class SwimInRisingWater {
    public static final int[][] directions = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

    public int swimInWater(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int row = grid.length, column = grid[0].length;
        boolean[][] visited = new boolean[row][column];
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[2] - b[2];
            }
        });
        visited[row - 1][column - 1] = true;
        pq.offer(new int[]{row - 1, column - 1, grid[row - 1][column - 1]});
        int maxTime = grid[row - 1][column - 1];
        boolean hasReachedSource = false;
        int startTime = grid[0][0];
        while (!pq.isEmpty() && !hasReachedSource) {
            int cell[] = pq.poll();
            maxTime = Math.max(maxTime, cell[2]);

            for (int dir[] : directions) {
                int nextX = cell[0] + dir[0];
                int nextY = cell[1] + dir[1];
                if (nextX >= 0 && nextX < row && nextY >= 0 && nextY < column && !visited[nextX][nextY]) {
                    if (nextX == 0 && nextY == 0) {
                        hasReachedSource = true;
                        break;
                    }
                    pq.offer(new int[] {nextX, nextY, grid[nextX][nextY]});
                    visited[nextX][nextY] = true;
                }
            }
        }
        return Math.max(maxTime, startTime);
    }
}
