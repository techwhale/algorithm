package org.practise.algorithm.interviewbit.hashing;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class EqualTest {

    private Equal obj = new Equal();

    @Test
    public void testEqualSum() {
        final ArrayList<Integer> list = new ArrayList<>(Arrays.asList(3, 4, 7, 1, 2, 9, 8));
        final List<Integer> result = obj.findEqualSum(list);
        final List<Integer> expected = new ArrayList<>(Arrays.asList(0, 2, 3, 5));
        Assert.assertEquals(result, expected);
    }

}