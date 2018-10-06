package org.practise.algorithm.interviewbit.string.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class LongestCommonPrefixTest {
    LongestCommonPrefix prefix = new LongestCommonPrefix();

    @Test
    public void testLongestCommonPrefix() {
        String[] strings = new String[] {  "abcdefgh", "aefghijk", "abcefgh"};
        List<String> strings1 = Arrays.asList(strings);
        String longestCommonPrefix = prefix.longestCommonPrefix(strings1);
        Assert.assertEquals(longestCommonPrefix, "a");
    }
}