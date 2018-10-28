package org.practise.algorithm.interviewbit.stackandqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class EvaluateExpressionTest {
    private EvaluateExpression obj = new EvaluateExpression();

    @Test
    public void testEvaluateExpression() {
        final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("2", "1", "+", "3", "*"));
        final int result = obj.evalRPN(arrayList);
        Assert.assertEquals(result, 9);
    }


    @Test
    public void testEvaluateExpression2() {
        final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("4", "13", "5", "/", "+"));
        final int result = obj.evalRPN(arrayList);
        Assert.assertEquals(result, 6);
    }
}