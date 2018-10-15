package org.practise.algorithm.leetcode.contest;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class KthSymbolInGrammarTest {
    private KthSymbolInGrammar obj = new KthSymbolInGrammar();

    @Test
    public void test1() {
        Assert.assertEquals(obj.kthGrammar(4, 5), 1);
    }

}