package org.practise.algorithm.leetcode.tree.medium;

import java.util.*;

public class WordSearchII {
//   Regular DFS Approach

//    public List<String> findWords(char[][] board, String[] words) {
//        Set<String> matchedWordList = new HashSet<>();
//        for (String word : words) {
//            if (exist(board, word)) {
//                matchedWordList.add(word);
//            }
//        }
//        return new ArrayList<>(matchedWordList);
//    }
//
//    public boolean exist(char[][] board, String word) {
//        char[] chars = word.toCharArray();
//        for (int i = 0; i < board.length; i++) {
//            for (int j = 0; j < board[0].length; j++) {
//                if (findWordExistence(board, i, j, 0, chars)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean findWordExistence(char[][] board, int i, int j, int position, char[] word) {
//        if (position == word.length) return true;
//        if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
//            return false;
//        }
//        if (board[i][j] != word[position] || board[i][j] == 0) {
//            return false;
//        }
//        char temp = board[i][j];
//        board[i][j] = 0;
//        boolean wordexist = ( findWordExistence(board, i - 1 , j, position + 1, word) ||
//                findWordExistence(board, i + 1 , j, position + 1, word) ||
//                findWordExistence(board, i, j - 1, position + 1, word) ||
//                findWordExistence(board, i, j + 1, position + 1, word));
//        board[i][j] = temp;
//        return wordexist;
//    }

//  DFS + Trie Approach

    public List<String> findWords(char[][] board, String[] words) {
        if (board == null || board.length == 0 || board[0].length == 0 || words == null || words.length == 0)
            return Collections.emptyList();
        Trie trie = new Trie();
        for (String word: words) {
            trie.insert(word);
        }
        Set<String> wordSet = new HashSet<>();
        final int N = board.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                boolean[][] visited = new boolean[N][N];
                dfs(i, j, N, board, visited,"", trie, wordSet);
            }
        }
        return new ArrayList<>(wordSet);
    }

    private int[][] directions = new int[][] {{0, 1}, {0, -1}, {1, 0}, {0, -1}};
    private void dfs(int x, int y, int N, char[][] board, boolean[][] visited, String word, Trie trie, Set<String> wordSet) {
        if (x < 0 || x >= N || y < 0 || y >= N || visited[x][y]) return;

        String newWord = word + board[x][y];

        if (! trie.startsWith(newWord)) return;

        if (trie.search(newWord)) {
            wordSet.add(newWord);
        }

        for (int[] dir : directions) {
           visited[x][y] = true;
           dfs(x + dir[0], y + dir[1], N, board, visited, newWord, trie, wordSet);
           visited[x][y] = false;
        }
    }


}
