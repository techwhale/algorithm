package org.practise.algorithm.leetcode.topologicalsorting;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AlienDictionaryTest {
    private AlienDictionary obj = new AlienDictionary();

    @Test
    public void testAlienDictionary() {
        String[] words = {"wrt","wrf","er","ett","rftt"};
        Assert.assertEquals(obj.alienOrder(words), "wertf");
    }
}