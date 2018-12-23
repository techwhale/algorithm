package org.practise.algorithm.leetcode.dfs.hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A 2d grid map of m rows and n columns is initially filled with water.
 * We may perform an addLand operation which turns the water at position (row, col) into a land. Given a list of positions to operate,
 * count the number of islands after each addLand operation.
 *
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
 * You may assume all four edges of the grid are all surrounded by water.
 *
 * Example:
 *
 * Input: m = 3, n = 3, positions = [[0,0], [0,1], [1,2], [2,1]]
 * Output: [1,1,2,3]
 * Explanation:
 *
 * Initially, the 2d grid grid is filled with water. (Assume 0 represents water and 1 represents land).
 *
 * 0 0 0
 * 0 0 0
 * 0 0 0
 * Operation #1: addLand(0, 0) turns the water at grid[0][0] into a land.
 *
 * 1 0 0
 * 0 0 0   Number of islands = 1
 * 0 0 0
 * Operation #2: addLand(0, 1) turns the water at grid[0][1] into a land.
 *
 * 1 1 0
 * 0 0 0   Number of islands = 1
 * 0 0 0
 * Operation #3: addLand(1, 2) turns the water at grid[1][2] into a land.
 *
 * 1 1 0
 * 0 0 1   Number of islands = 2
 * 0 0 0
 * Operation #4: addLand(2, 1) turns the water at grid[2][1] into a land.
 *
 * 1 1 0
 * 0 0 1   Number of islands = 3
 * 0 1 0
 * Follow up:
 *
 * Can you do it in time complexity O(k log mn), where k is the length of the positions?
 */
public class NumberOfIslandsII {
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        DSU dsu = new DSU(m * n);

        List<Integer> resultList = new ArrayList<>();

        // iterate position
        for (int[] position : positions) {
            int row = position[0], column = position[1], index = row * n + column;

            // if this island is already used
            if (dsu.isValid(index)) {
                resultList.add(dsu.getDisjointedGroupCount());
                continue;
            }

            List<Integer> overlapList = new ArrayList<>();

            // iterate horizon & vertical direction for overlap
            for (int[] dir : DIRECTIONS) {
                int r = row + dir[0], c = column + dir[1];
                if (r < 0 || c < 0 || r >= m || c >= n) continue;
                if (dsu.isValid(r * n + c)) {
                    overlapList.add(r * n + c);
                }
            }

            dsu.setParent(index);

            // make this island as parent for overlapping island
            for (int island : overlapList) {
                dsu.union(index, island);
            }

            resultList.add(dsu.getDisjointedGroupCount());
        }
        return resultList;
    }

    class DSU {
        int[] parent;
        int[] rank;
        int disjointed_group_count = 0;
        public DSU(int N) {
            parent = new int[N];
            rank = new int[N];
            Arrays.fill(parent, -1);
        }

        public boolean isValid(int i) {
            return parent[i] >= 0;
        }

        public int find(int x) {
            if (x != parent[x]) parent[x] = find(parent[x]);
            return parent[x];
        }

        public void setParent(int i) {
            parent[i] = i;
            ++disjointed_group_count;
        }

        public void union(int x, int y) {
            int rootx = find(x), rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                    rank[rootx]++;
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                    rank[rooty]++;
                } else {
                    parent[rooty] = rootx;
                    rank[rootx]++;
                }
                --disjointed_group_count;
            }
        }

        public int getDisjointedGroupCount() {
            return disjointed_group_count;
        }
    }
}
