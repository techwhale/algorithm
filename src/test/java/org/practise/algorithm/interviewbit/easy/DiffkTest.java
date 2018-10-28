package org.practise.algorithm.interviewbit.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class DiffkTest {
    private Diffk diffk = new Diffk();

    @Test
    public void testDiffK() {
        Assert.assertEquals(diffk.diffPossible(Arrays.asList(1, 2, 3), 0), 0);
    }

    @Test
    public void testDiffK2() {
        Assert.assertEquals(diffk.diffPossible(Arrays.asList(16, 23, 47, 51, 51, 68, 76, 84, 85, 87, 91), 60), 1);
    }
}