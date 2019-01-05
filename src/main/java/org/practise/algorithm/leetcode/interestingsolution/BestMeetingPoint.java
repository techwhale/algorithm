package org.practise.algorithm.leetcode.interestingsolution;

import java.util.ArrayList;
import java.util.List;

/**
 * 296. Best Meeting Point
 * A group of two or more people wants to meet and minimize the total travel distance. You are given a 2D grid of values 0 or 1,
 * where each 1 marks the home of someone in the group. The distance is calculated using Manhattan Distance,
 * where distance(p1, p2) = |p2.x - p1.x| + |p2.y - p1.y|.
 *
 * Example:
 *
 * Input:
 *
 * 1 - 0 - 0 - 0 - 1
 * |   |   |   |   |
 * 0 - 0 - 0 - 0 - 0
 * |   |   |   |   |
 * 0 - 0 - 1 - 0 - 0
 *
 * Output: 6
 *
 * Explanation: Given three people living at (0,0), (0,4), and (2,2):
 *              The point (0,2) is an ideal meeting point, as the total travel distance
 *              of 2+2+2=6 is minimal. So return 6.
 */
public class BestMeetingPoint {
    public int minTotalDistance(int[][] grid) {
        List<Integer> rowList = collectRows(grid);
        List<Integer> columnList = collectColumns(grid);
        return findOneDimensionalDistance(rowList) + findOneDimensionalDistance(columnList);
    }

    private int findOneDimensionalDistance(List<Integer> list) {
        int distance = 0;
        int left_index = 0, right_index = list.size() - 1;

        while (left_index < right_index) {
            int low = list.get(left_index);
            int high = list.get(right_index);
            left_index++; right_index--;
            distance += Math.abs(high - low);
        }
        return distance;
    }

    private List<Integer> collectColumns(int[][] grid) {
        List<Integer> columnList = new ArrayList<>();
        for (int column= 0; column < grid[0].length; column++) {
            for (int row = 0; row < grid.length; row++) {
                if (grid[row][column] == 1) {
                    columnList.add(column);
                }
            }
        }
        return columnList;
    }
    private List<Integer> collectRows(int[][] grid) {
        List<Integer> rowList = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[0].length; column++) {
                if (grid[row][column] == 1) {
                    rowList.add(row);
                }
            }
        }
        return rowList;
    }
}
