package org.practise.algorithm.interestingideas;

/**
 * According to the Wikipedia's article: "The Game of Life, also known simply as Life, is a cellular automaton devised
 * by the British mathematician John Horton Conway in 1970."
 *
 * Given a board with m by n cells, each cell has an initial state live (1) or dead (0).
 * Each cell interacts with its eight neighbors (horizontal, vertical, diagonal) using the following four rules (taken from the above Wikipedia article):
 *
 * Any live cell with fewer than two live neighbors dies, as if caused by under-population.
 * Any live cell with two or three live neighbors lives on to the next generation.
 * Any live cell with more than three live neighbors dies, as if by over-population..
 * Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 * Write a function to compute the next state (after one update) of the board given its current state.
 * The next state is created by applying the above rules simultaneously to every cell in the current state, where births and deaths occur simultaneously.
 *
 * Example:
 *
 * Input:
 * [
 *   [0,1,0],
 *   [0,0,1],
 *   [1,1,1],
 *   [0,0,0]
 * ]
 * Output:
 * [
 *   [0,0,0],
 *   [1,0,1],
 *   [0,1,1],
 *   [0,1,0]
 * ]
 * Follow up:
 *
 * Could you solve it in-place? Remember that the board needs to be updated at the same time:
 * You cannot update some cells first and then use their updated values to update other cells.
 * In this question, we represent the board using a 2D array. In principle, the board is infinite,
 * which would cause problems when the active area encroaches the border of the array. How would you address these problems?
 */
public class GameOfLife {
    public void gameOfLife(int[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                final int neighboursCount = getNeighboursCount(board, row, column);
                if ((board[row][column] & 1) == 1) {
                    if (2 <= neighboursCount && neighboursCount <= 3)
                        board[row][column] = 3; // 11
                } else {
                    if (neighboursCount == 3) {
                        board[row][column] = 2; // 10
                    }
                }
            }
        }

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                board[row][column] >>= 1;
            }
        }
    }

    private int getNeighboursCount(int[][] board, int row, int column) {
        int maxRow = board.length, maxColumn = board[0].length;
        int count = 0;
        for (int r = row - 1; r <= row + 1; r++){
            for (int c = column - 1; c <= column + 1; c++) {
                if (r < 0 || r >= maxRow || c < 0 || c >= maxColumn) continue;
                count += board[r][c] & 1;
            }
        }
        count -= board[row][column] & 1;
        return count;
    }
}
