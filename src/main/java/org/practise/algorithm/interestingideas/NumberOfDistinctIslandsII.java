package org.practise.algorithm.interestingideas;

import java.util.*;

/**
 * Given a non-empty 2D array grid of 0's and 1's, an island is a group of 1's (representing land) connected 4-directionally (horizontal or vertical.)
 * You may assume all four edges of the grid are surrounded by water.
 *
 * Count the number of distinct islands. An island is considered to be the same as another if they have the same shape, or
 * have the same shape after rotation (90, 180, or 270 degrees only) or reflection (left/right direction or up/down direction).
 *
 * Example 1:
 * 11000
 * 10000
 * 00001
 * 00011
 * Given the above grid map, return 1.
 *
 * Notice that:
 * 11
 * 1
 * and
 *  1
 * 11
 * are considered same island shapes. Because if we make a 180 degrees clockwise rotation on the first island, then two islands will have the same shapes.
 * Example 2:
 * 11100
 * 10001
 * 01001
 * 01110
 * Given the above grid map, return 2.
 *
 * Here are the two distinct islands:
 * 111
 * 1
 * and
 * 1
 * 1
 *
 * Notice that:
 * 111
 * 1
 * and
 * 1
 * 111
 * are considered same island shapes. Because if we flip the first array in the up/down direction, then they have the same shapes.
 * Note: The length of each dimension in the given grid does not exceed 50.
 */
public class NumberOfDistinctIslandsII {
    private final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private final int[][] REFLECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public int numDistinctIslands2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        Set<String> set = new HashSet<>();

        List<Point> island = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                dfs(grid, i, j, island);
                if (! island.isEmpty()) {
                    set.add(generateCanonicalHash(island));
                    island.clear();
                }
            }
        }
        return set.size();
    }

    private String generateCanonicalHash(List<Point> points) {
        List<Point>[] combination = new ArrayList[8];
        for (int i = 0; i < REFLECTIONS.length; i++) {
            combination[i] = new ArrayList<>();
            combination[i + 4] = new ArrayList<>();
            int[] reflection = REFLECTIONS[i];
            for (Point p : points) {
                combination[i].add(new Point(p.x * reflection[0], p.y * reflection[1]));
                combination[i + 4].add(new Point(p.y * reflection[0], p.x * reflection[1]));
            }
        }

        for (int i = 0; i < combination.length; i++) {
            Collections.sort(combination[i]);
        }

        String hashes[] = new String[8];

        for (int i = 0; i < combination.length; i++) {
            StringBuilder builder = new StringBuilder();
            final Point firstPoint = combination[i].get(0);
            int minX = firstPoint.x, minY = firstPoint.y;

            for (Point p : combination[i]) {
                builder.append(p.x - minX);
                builder.append(",");
                builder.append(p.y - minY);
                builder.append(";");
            }
            hashes[i] = builder.toString();
        }
        Arrays.sort(hashes);
        return hashes[0];
    }

    private void dfs(int[][] grid, int row, int col, List<Point> list) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length || grid[row][col] != 1) return;
        grid[row][col] = 2;
        list.add(new Point(row, col));
        for (int[] direction : DIRECTIONS) {
            dfs(grid, row + direction[0], col + direction[1], list);
        }
    }


    public class Point implements Comparable<Point> {
        int x;
        int y;
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point p) {
            return this.x - p.x == 0 ? this.y - p.y : this.x - p.x;
        }
    }
}
