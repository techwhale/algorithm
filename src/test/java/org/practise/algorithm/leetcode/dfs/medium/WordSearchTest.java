package org.practise.algorithm.leetcode.dfs.medium;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WordSearchTest {
    WordSearch obj = new WordSearch();
    private char[][] board;

    @BeforeMethod
    public void reset() {
        board = new char[][]{
                { 'A', 'B', 'C', 'E' },
                { 'S', 'F', 'C', 'S' },
                { 'A', 'D', 'E', 'E' }};
    }

    @Test
    public void testWordSearch1() {
        String word = "ABCCED";
        boolean exist = obj.exist(board, word);
        Assert.assertEquals(exist, true);
    }

    @Test
    public void testWordSearch2() {
        String word = "SEE";
        boolean exist = obj.exist(board, word);
        Assert.assertEquals(exist, true);
    }

    @Test
    public void testWordSearch3() {
        String word = "ABCB";
        boolean exist = obj.exist(board, word);
        Assert.assertEquals(exist, false);
    }
}