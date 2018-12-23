package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class WordBreakTest {
    private WordBreak wordBreak = new WordBreak();

    @Test
    public void testWordBreak() {
        final List<String> dictionaryWords = Arrays.asList("i", "like", "sam", "sung", "samsung", "mobile", "ice", "cream", "icecream", "man", "go", "mango");
        final int result = wordBreak.wordBreak("manigo", dictionaryWords);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testWordBreak2() {
        final List<String> dictionaryWords = Arrays.asList("interview", "my", "trainer");
        final int result = wordBreak.wordBreak("myinterviewtrainer", dictionaryWords);
        Assert.assertEquals(result, 1);
    }
}