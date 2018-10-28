package org.practise.algorithm.interviewbit.stackandqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BracesTest {
    private Braces obj = new Braces();

    @Test
    public void testBraces() {
        String braces = "((a + b))";
        Assert.assertEquals(obj.braces(braces), 1);
    }

    @Test
    public void testBraces2() {
        String braces = "(a + (a + b))";
        Assert.assertEquals(obj.braces(braces), 0);
    }

    @Test
    public void testBraces3() {
        String braces = "a+b";
        Assert.assertEquals(obj.braces(braces), 0);
    }
}