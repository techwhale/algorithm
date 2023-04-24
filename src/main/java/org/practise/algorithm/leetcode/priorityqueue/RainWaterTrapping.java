package org.practise.algorithm.leetcode.priorityqueue;

/*
Trapping Rain Water II

https://leetcode.com/problems/trapping-rain-water-ii/description/

Given an m x n matrix of positive integers representing the height of each unit cell in a 2D elevation map, compute the volume of water it is able to trap after raining.

Note:
Both m and n are less than 110. The height of each unit cell is greater than 0 and is less than 20,000.

Example:

Given the following 3x6 height map:
[
  [1,4,3,1,3,2],
  [3,2,1,3,2,4],
  [2,3,3,2,3,1]
]

Return 4.


After the rain, water is trapped between the blocks. The total volume of water trapped is 4.

*/

import java.util.Comparator;
import java.util.PriorityQueue;

public class RainWaterTrapping {
    public static final int[][] dirs = new int[][] {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    public int trapRainWater(int[][] heightMap) {
        if (heightMap == null || heightMap.length < 1 || heightMap[0].length < 1) {
            return 0;
        }
        PriorityQueue<int[]> pq = new PriorityQueue((Comparator<int[]>) (a, b) -> a[2] - b[2]);
        int row = heightMap.length, col = heightMap[0].length;
        boolean[][] visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 0 || j == 0 || i == row - 1 || j == col -1) {
                    visited[i][j] = true;
                    pq.offer(new int[] {i , j, heightMap[i][j]});
                }
            }
        }

        int maxWaterTrapped = 0;
        while (! pq.isEmpty()) {
            int[] cell = pq.poll();
            for (int[] dir : dirs) {
                int nX = cell[0] + dir[0];
                int nY = cell[1] + dir[1];
                if(nX >= 0 && nX < row && nY >= 0 && nY < col && !visited[nX][nY]) {
                    maxWaterTrapped += Math.max(0, cell[2] - heightMap[nX][nY]);
                    pq.offer(new int[] {nX, nY, Math.max(heightMap[nX][nY], cell[2])});
                    visited[nX][nY] = true;
                }
            }
        }
        return maxWaterTrapped;
    }
}
