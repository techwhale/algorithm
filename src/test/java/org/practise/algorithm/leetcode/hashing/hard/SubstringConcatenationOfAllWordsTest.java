package org.practise.algorithm.leetcode.hashing.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class SubstringConcatenationOfAllWordsTest {
    private SubstringConcatenationOfAllWords obj = new SubstringConcatenationOfAllWords();

    @Test
    public void testSubstringConcatenationOfAllWords1() {
        final List<Integer> resultList = obj.findSubstring("barfoothefoobarman", new String[]{"foo", "bar"});
        Assert.assertEquals(resultList, Arrays.asList(0, 9));
    }

    @Test
    public void testSubstringConcatenationOfAllWords2() {
        final List<Integer> resultList = obj.findSubstring("wordgoodstudentgoodword", new String[]{"word", "student"});
        Assert.assertTrue(resultList.isEmpty());
    }
}