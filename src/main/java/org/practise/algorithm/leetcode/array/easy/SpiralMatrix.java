package org.practise.algorithm.leetcode.array.easy;

import java.util.ArrayList;
import java.util.List;

/**
 * 54. Spiral Matrix
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an m x n matrix, return all elements of the matrix in spiral order.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [1,2,3,6,9,8,7,4,5]
 * Example 2:
 *
 *
 * Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 10
 * -100 <= matrix[i][j] <= 100
 */
public class SpiralMatrix {
    public List<Integer> spiralOrder(int[][] matrix) {
        int M = matrix.length, N = matrix[0].length;
        int left = 0;
        int up = 0;
        int right = N - 1;
        int down = M - 1;
        List<Integer> result = new ArrayList<>();
        while (result.size() < M * N) {
            for (int col = left; col <= right; col++) {
                result.add(matrix[up][col]);
            }
            for (int row = up + 1; row <= down; row++) {
                result.add(matrix[row][right]);
            }
            if (up != down) {
                for (int col = right - 1; col >= left; col --) {
                    result.add(matrix[down][col]);
                }
            }
            if (left != right) {
                for (int row = down - 1; row > up; row --) {
                    result.add(matrix[row][left]);
                }
            }
            left++;
            up++;
            right--;
            down--;
        }
        return result;
    }
}
