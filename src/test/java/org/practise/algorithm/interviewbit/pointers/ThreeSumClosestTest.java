package org.practise.algorithm.interviewbit.pointers;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class ThreeSumClosestTest {
    private ThreeSumClosest obj = new ThreeSumClosest();

    @Test
    public void testThreeSumClosest() {
        final int[] values = {-5, 1, 4, -7, 10, -7, 0, 7, 3, 0, -2, -5, -3, -6, 4, -7, -8, 0, 4, 9, 4, 1, -8,
                -6, -6, 0, -9, 5, 3, -9, -5, -9, 6, 3, 8, -10, 1, -2, 2, 1, -9, 2, -3, 9, 9, -10, 0, -9, -2,
                7, 0, -4, -3, 1, 6, -3};
        final List<Integer> list = Arrays.stream(values).boxed().collect(Collectors.toList());
        Assert.assertEquals(obj.threeSumClosest(list, -1), -1);
    }


    @Test
    public void testThreeSumClosest2() {
        final int[] values = {-1, 2, 1, -4};
        final List<Integer> list = Arrays.stream(values).boxed().collect(Collectors.toList());
        Assert.assertEquals(obj.threeSumClosest(list, 1), 2);
    }

}