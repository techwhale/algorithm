package org.practise.algorithm.leetcode.priorityqueue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class SwimInRisingWater {
    private static final int[][] DIRECTION = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    public int swimInWater(int[][] grid) {
        int N = grid.length;
        boolean[][] visited = new boolean[N][N];
        int startTime = grid[0][0], maxTime = startTime;
        boolean reachedDestination = false;
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[2] - b[2];
            }
        });
        pq.offer(new int[] {0, 0, startTime});
        visited[0][0] = true;
        while(!pq.isEmpty() && !reachedDestination) {
            int[] points = pq.poll();
            int row = points[0], col = points[1];
            maxTime = Math.max(maxTime, points[2]);
            visited[row][col] = true;
            if (row == N - 1 && col == N - 1) {
                reachedDestination = true;
                break;
            }
            for (int[] dir : DIRECTION) {
                int rowX = row + dir[0], colY = col + dir[1];
                if (rowX < 0 || colY < 0 || rowX >= N || colY >= N || visited[rowX][colY]) continue;
                pq.offer(new int[] {rowX, colY, grid[rowX][colY]});
            }
        }
        return maxTime;
    }
}
