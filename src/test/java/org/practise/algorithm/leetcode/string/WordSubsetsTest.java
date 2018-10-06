package org.practise.algorithm.leetcode.string;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class WordSubsetsTest {
    WordSubsets wordSubsets = new WordSubsets();
    @Test
    public void testWordSubset() {
        String[] A = new String[] {"amazon","apple","facebook","google","leetcode"}, B = {"ec","oc","ceo"};
        List<String> expectedResult = Arrays.asList(new String[]  {"facebook", "leetcode"});
        List<String> actualResult = wordSubsets.wordSubsets(A, B);
        Assert.assertEquals(actualResult, expectedResult);
    }
}