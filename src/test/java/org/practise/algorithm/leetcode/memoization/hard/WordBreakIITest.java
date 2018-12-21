package org.practise.algorithm.leetcode.memoization.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class WordBreakIITest {
    private WordBreakII obj = new WordBreakII();

    @Test
    public void testWordBreak() {
        final String[] dictionary = {"apple", "pen", "applepen", "pine", "pineapple"};
        final List<String> sentenceList = obj.wordBreak("pineapplepenapple", Arrays.asList(dictionary));
        Assert.assertEquals(sentenceList.size(), 3);
        Assert.assertTrue(sentenceList.contains("pine apple pen apple"));
        Assert.assertTrue(sentenceList.contains("pineapple pen apple"));
        Assert.assertTrue(sentenceList.contains("pine applepen apple"));

    }
}