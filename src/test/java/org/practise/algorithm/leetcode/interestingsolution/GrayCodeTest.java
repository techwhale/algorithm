package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class GrayCodeTest {
    private GrayCode obj = new GrayCode();

    @Test
    public void testGrayCode() {
        Assert.assertEquals(obj.grayCode(3), Arrays.asList(0, 1, 3 ,2, 6, 7, 5, 4));
    }
}