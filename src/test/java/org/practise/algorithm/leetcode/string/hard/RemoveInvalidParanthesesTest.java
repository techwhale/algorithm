package org.practise.algorithm.leetcode.string.hard;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class RemoveInvalidParanthesesTest {

    RemoveInvalidParantheses obj = new RemoveInvalidParantheses();
    @Test
    public void testRemoveInvalidParentheses() {
//        List<String> actual = obj.removeInvalidParentheses("()())()");
//        Assert.assertEquals(actual, Arrays.asList("()()()", "(())()"));

        Assert.assertEquals(obj.removeInvalidParentheses("x("),  Arrays.asList("x"));
    }


}