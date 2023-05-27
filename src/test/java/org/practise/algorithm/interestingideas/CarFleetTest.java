package org.practise.algorithm.interestingideas;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CarFleetTest {
    CarFleet obj = new CarFleet();
    @Test
    public void testCarFleet() {
        Assert.assertEquals(obj.carFleet(12, new int[]{10,8,0,5,3}, new int[]{2,4,1,1,3}), 3);
        Assert.assertEquals(obj.carFleet(100, new int[]{0,2,4}, new int[]{4,2,1}), 1);
    }
}