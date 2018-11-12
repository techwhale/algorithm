package org.practise.algorithm.leetcode.dfs.medium;

import org.practise.algorithm.leetcode.tree.medium.WordSearchII;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class WordSearchIITest {
    WordSearchII obj = new WordSearchII();

    private char[][] board;

    @BeforeMethod
    public void reset() {
        board = new char[][]
                       {{'o','a','a','n'},
                        {'e','t','a','e'},
                        {'i','h','k','r'},
                        {'i','f','l','v'}};
    }

    @Test
    public void testWordSearch() {
        String[] words = {"oath","pea","eat","rain"};
        List<String> matchedWordList = obj.findWords(board, words);
        Assert.assertEquals(matchedWordList, Arrays.asList("oath", "eat"));
    }

}