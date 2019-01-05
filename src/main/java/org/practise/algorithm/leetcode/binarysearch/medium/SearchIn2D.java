package org.practise.algorithm.leetcode.binarysearch.medium;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
 *
 * Integers in each row are sorted from left to right.
 * The first integer of each row is greater than the last integer of the previous row.
 * Example 1:
 *
 * Input:
 * matrix = [
 *   [1,   3,  5,  7],
 *   [10, 11, 16, 20],
 *   [23, 30, 34, 50]
 * ]
 * target = 3
 * Output: true
 * Example 2:
 *
 * Input:
 * matrix = [
 *   [1,   3,  5,  7],
 *   [10, 11, 16, 20],
 *   [23, 30, 34, 50]
 * ]
 * target = 13
 * Output: false
 */
public class SearchIn2D {
    public boolean searchMatrix(int[][] matrix, int target) {
        int row = findRow(matrix, target), column = findColumn(matrix[row], target);
        return matrix[row][column] == target;
    }
    private int findColumn(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        while (low < high) {
            int mid = (low + high)/ 2;
            if (arr[mid] == target)
                return mid;
            else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }
    private int findRow(int[][] matrix, int target) {
        int low = 0, high = matrix.length - 1, lastColumn = matrix[0].length - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (matrix[mid][lastColumn] == target)
                return mid;
            else if(matrix[mid][lastColumn] > target) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }
}
