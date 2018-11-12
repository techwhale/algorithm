package org.practise.algorithm.interviewbit.backtracking;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

public class NQueensTest {
    private NQueens obj = new NQueens();

    @Test
    public void testNQueen1() {
        final ArrayList<ArrayList<String>> resultList = obj.solveNQueens(2);
        Assert.assertTrue(resultList.isEmpty());
    }

    @Test
    public void testNQueen3() {
        final ArrayList<ArrayList<String>> resultList = obj.solveNQueens(3);
        Assert.assertTrue(resultList.isEmpty());
    }

}