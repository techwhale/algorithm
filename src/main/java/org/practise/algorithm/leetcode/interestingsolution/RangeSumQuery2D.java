package org.practise.algorithm.leetcode.interestingsolution;
/**
 *308. Range Sum Query 2D - Mutable
 * Hard
 *
 * Given a 2D matrix matrix, handle multiple queries of the following types:
 *
 * Update the value of a cell in matrix.
 * Calculate the sum of the elements of matrix inside the rectangle defined by its upper left corner (row1, col1) and lower right corner (rows, col2).
 * Implement the NumMatrix class:
 *
 * NumMatrix(int[][] matrix) Initializes the object with the integer matrix matrix.
 * void update(int row, int col, int val) Updates the value of matrix[row][col] to be val.
 * int sumRegion(int row1, int col1, int rows, int col2) Returns the sum of the elements of matrix inside the rectangle defined by its upper left corner (row1, col1) and lower right corner (rows, col2).
 *
 * Example 1:
 *
 * Input
 * ["NumMatrix", "sumRegion", "update", "sumRegion"]
 * [[[[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]], [2, 1, 4, 3], [3, 2, 2], [2, 1, 4, 3]]
 * Output
 * [null, 8, null, 10]
 *
 * Explanation
 * NumMatrix numMatrix = new NumMatrix([[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]);
 * numMatrix.sumRegion(2, 1, 4, 3); // return 8 (i.e. sum of the left red rectangle)
 * numMatrix.update(3, 2, 2);       // matrix changes from left image to right image
 * numMatrix.sumRegion(2, 1, 4, 3); // return 10 (i.e. sum of the right red rectangle)
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 200
 * -1000 <= matrix[i][j] <= 1000
 * 0 <= row < m
 * 0 <= col < n
 * -1000 <= val <= 1000
 * 0 <= row1 <= rows < m
 * 0 <= col1 <= col2 < n
 * At most 5000 calls will be made to sumRegion and update
 *
 */
public class RangeSumQuery2D {
        int[][] bit;
        int rows;
        int cols;
        public RangeSumQuery2D(int[][] matrix) {
            rows = matrix.length;
            cols = matrix[0].length;
            bit = new int[rows + 1][cols + 1];
            buildBIT(matrix);
        }

        public void update(int row, int col, int val) {
            int prevValue = sumRegion(row, col, row, col);
            int diff = val - prevValue;
            row++; col++;
            updateBIT(row, col, diff);
        }


        public int sumRegion(int row1, int col1, int row2, int col2) {
            // BITS start index is at 1
            row1++; col1++; row2++; col2++;
            return getValue(row2, col2) + getValue(row1 - 1, col1 - 1) - getValue(row2, col1 - 1) - getValue(row1 - 1, col2);
        }


        private int getValue(int row, int column) {
            int sum = 0;
            for (int i = row; i > 0; i -= lsb(i)) {
                for (int j = column; j > 0; j -= lsb(j)) {
                    sum += bit[i][j];
                }
            }
            return sum;
        }

        private int lsb(int n) {
            return n & -n;
        }

        private void updateBIT(int row, int column, int value){
            for (int i = row; i <= rows; i += lsb(i)){
                for (int j = column; j <= cols; j += lsb(j)) {
                    bit[i][j] += value;
                }
            }
        }

        private void buildBIT(int[][] matrix){
            for (int i = 1; i <= rows; i++) {
                for (int j = 1; j <= cols; j++) {
                    updateBIT(i, j, matrix[i - 1][j - 1]);
                }
            }
        }

/**
 * Your NumMatrix object will be instantiated and called as such:
 * NumMatrix obj = new NumMatrix(matrix);
 * obj.update(row,col,val);
 * int param_2 = obj.sumRegion(row1,col1,rows,col2);
 */
}
