package org.practise.algorithm.leetcode.array.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BoatsToSavePeopleTest {

    BoatsToSavePeople obj = new BoatsToSavePeople();

    @Test
    public void testBoatsToSavePeople(){
        Assert.assertEquals(obj.numRescueBoats(new int[]{3,2,2,1}, 3), 3);
        Assert.assertEquals(obj.numRescueBoats(new int[]{3,5,3,4}, 5), 4);
    }

}