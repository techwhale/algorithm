package org.practise.algorithm.interviewbit.binarysearch.easy;

import java.util.ArrayList;

/*
Write an efficient algorithm that searches for a value in an m x n matrix.

This matrix has the following properties:

Integers in each row are sorted from left to right.
The first integer of each row is greater than or equal to the last integer of the previous row.
Example:

Consider the following matrix:

[
  [1,   3,  5,  7],
  [10, 11, 16, 20],
  [23, 30, 34, 50]
]
Given target = 3, return 1 ( 1 corresponds to true )

Return 0 / 1 ( 0 if the element is not present, 1 if the element is present ) for this problem

 */
public class MatrixSearch {
    public int searchMatrix(ArrayList<ArrayList<Integer>> a, int b) {
        int possibleRow = findRow(a, b);
        ArrayList<Integer> possibleList = a.get(possibleRow);
        int possiblePosition = getPossiblePosition(possibleList, b);
        return (possibleList.get(possiblePosition) == b) ? 1 : 0;
    }

    // 0 - not present, 1 - present
    private int getPossiblePosition(ArrayList<Integer> list, int b) {
        int min = 0;
        int max = list.size() - 1;
        while (min < max) {
            int mid = (min + max) / 2;
            int value = list.get(mid);
            if (value == b) {
                return mid;
            } else if (value > b) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        return min;
    }

    private int findRow(ArrayList<ArrayList<Integer>> a, int b) {
        int minRow = 0;
        int maxRow = a.size() - 1;
        while (minRow < maxRow) {
            int midRow = (maxRow + minRow)/ 2;
            ArrayList<Integer> midRowList = a.get(midRow);
            int maxRowValue = midRowList.get(midRowList.size() - 1);
            if (maxRowValue == b) {
                return midRow;
            } else if (maxRowValue < b) {
                minRow = midRow + 1;
            }  else {
                maxRow = midRow;
            }
        }
        return minRow;
    }
}
