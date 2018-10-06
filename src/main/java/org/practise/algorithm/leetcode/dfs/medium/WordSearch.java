package org.practise.algorithm.leetcode.dfs.medium;

/**

 Given a 2D board and a word, find if the word exists in the grid.

 The word can be constructed from letters of sequentially adjacent cell, where "adjacent" cells are those horizontally or vertically neighboring. The same letter cell may not be used more than once.

 Example:

 board =
 [
 ['A','B','C','E'],
 ['S','F','C','S'],
 ['A','D','E','E']
 ]

 Given word = "ABCCED", return true.
 Given word = "SEE", return true.
 Given word = "ABCB", return false.
 */
public class WordSearch {
    public boolean exist(char[][] board, String word) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (findWordExistence(board, i, j, 0, chars)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean findWordExistence(char[][] board, int i, int j, int position, char[] word) {
        if (position == word.length) return true;
        if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
            return false;
        }
        if (board[i][j] != word[position] || board[i][j] == 0) {
            return false;
        }
        char temp = board[i][j];
        board[i][j] = 0;
        boolean wordexist = ( findWordExistence(board, i - 1 , j, position + 1, word) ||
                              findWordExistence(board, i + 1 , j, position + 1, word) ||
                              findWordExistence(board, i, j - 1, position + 1, word) ||
                              findWordExistence(board, i, j + 1, position + 1, word));
        board[i][j] = temp;
        return wordexist;
    }
}
