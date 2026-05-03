package org.practise.algorithm.leetcode.dfs.easy;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class WordSearchTest {

    public static void main(String args[]) {
        WordSearch wordSearch = new WordSearch();
        char[][] charArray = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'E', 'S'},
                {'A', 'D', 'E', 'E'}
        };
        wordSearch.exist(charArray, "ABCESEEEFS");
    }
//    @Test
//    public void testExist() {
//    }
}