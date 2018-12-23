package org.practise.algorithm.leetcode.string.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class WordLadderTest {
    private WordLadder obj = new WordLadder();

    @Test
    public void testWordLadder1() {
        final String[] wordLadder = {"hot", "dot", "dog", "lot", "log", "cog"};
        final int ladderLength = obj.ladderLength("hit", "cog", Arrays.asList(wordLadder));
        Assert.assertEquals(ladderLength, 5);
    }

    @Test
    public void testWordLadder2() {
        final String[] wordLadder = {"hot", "dot", "dog", "lot", "log"};
        final int ladderLength = obj.ladderLength("hit", "cog", Arrays.asList(wordLadder));
        Assert.assertEquals(ladderLength, 0);
    }
}