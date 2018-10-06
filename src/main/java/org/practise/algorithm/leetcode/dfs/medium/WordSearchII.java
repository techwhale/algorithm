package org.practise.algorithm.leetcode.dfs.medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordSearchII {
    public List<String> findWords(char[][] board, String[] words) {
        Set<String> matchedWordList = new HashSet<>();
        for (String word : words) {
            if (exist(board, word)) {
                matchedWordList.add(word);
            }
        }
        return new ArrayList<>(matchedWordList);
    }

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
