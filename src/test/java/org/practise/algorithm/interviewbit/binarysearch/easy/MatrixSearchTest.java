package org.practise.algorithm.interviewbit.binarysearch.easy;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class MatrixSearchTest {
    MatrixSearch matrixSearch = new MatrixSearch();

    @Test
    public void testMatrixSearch() {
        int[][] matrix = new int[][] {
                {3, 3, 11, 12, 14},
                {16, 17, 30, 34, 35},
                {45, 48, 49, 50, 52},
                {56, 59, 63, 63, 65},
                {67, 71, 72, 73, 79},
                {80, 84, 85, 85, 88},
                {88, 91, 92, 93, 94}
        };

        ArrayList<ArrayList<Integer>> matrix2dList = twoDArrayToList(matrix);
        int result = matrixSearch.searchMatrix(matrix2dList, 94);
        Assert.assertEquals(result, matrix);
    }

    private ArrayList<ArrayList<Integer>> twoDArrayToList(int[][] twoDArray) {
        ArrayList<ArrayList<Integer>> arrayList2D = new ArrayList<>();
        for (int i = 0; i < twoDArray.length; i++) {
            ArrayList<Integer> eachRecord = new ArrayList<>();
            for (int j = 0; j < twoDArray[i].length; j++) {
                eachRecord.add(twoDArray[i][j]);
            }
            arrayList2D.add(eachRecord);
        }
        return arrayList2D;
    }

}