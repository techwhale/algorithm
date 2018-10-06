package org.practise.algorithm.interviewbit.string.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CountAndSayTest {
    CountAndSay countAndSay = new CountAndSay();

    @Test
    public void testCountAndSay() {
        String s = countAndSay.countAndSay(6);
        Assert.assertEquals(s, "312211");
    }
}