package org.practise.algorithm.interviewbit.easy;

import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class IntersectionOfSortedArraysTest {
    private IntersectionOfSortedArrays obj = new IntersectionOfSortedArrays();

    @Test
    public void testIntersectionOfSortedArrays() {
        obj.intersect(Arrays.asList(10000000), Arrays.asList(10000000));
    }
}