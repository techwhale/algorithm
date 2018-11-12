package org.practise.algorithm.leetcode.contest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReorderLogFilesTest {
    private ReorderLogFiles reorderLogFiles = new ReorderLogFiles();
    @Test
    public void testReorderLogFiles() {
        final String[] input = {"a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"};
        final String[] result = reorderLogFiles.reorderLogFiles(input);
        Assert.assertEquals(result, new String[] {"g1 act car","a8 act zoo","ab1 off key dog","a1 9 2 3 1","zo4 4 7"});
    }
}