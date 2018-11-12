package org.practise.algorithm.interviewbit.hashing;

import java.util.*;

/**
 * Given an array A of integers, find the index of values that satisfy A + B = C + D, where A,B,C & D are integers values in the array
 *
 * Note:
 *
 * 1) Return the indices `A1 B1 C1 D1`, so that
 *   A[A1] + A[B1] = A[C1] + A[D1]
 *   A1 < B1, C1 < D1
 *   A1 < C1, B1 != D1, B1 != C1
 *
 * 2) If there are more than one solutions,
 *    then return the tuple of values which are lexicographical smallest.
 *
 * Assume we have two solutions
 * S1 : A1 B1 C1 D1 ( these are values of indices int the array )
 * S2 : A2 B2 C2 D2
 *
 * S1 is lexicographically smaller than S2 iff
 *   A1 < A2 OR
 *   A1 = A2 AND B1 < B2 OR
 *   A1 = A2 AND B1 = B2 AND C1 < C2 OR
 *   A1 = A2 AND B1 = B2 AND C1 = C2 AND D1 < D2
 * Example:
 *
 * Input: [3, 4, 7, 1, 2, 9, 8]
 * Output: [0, 2, 3, 5] (O index)
 * If no solution is possible, return an empty list.
 */
public class Equal {
    public List<Integer> findEqualSum(List<Integer> list) {
        List<Integer> resultList = new ArrayList<>();

        if (list == null || list.size() < 4) {
            return resultList;
        }

        Map<Integer, int[]> sumToIndex = new HashMap<>();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                int sum = list.get(i) + list.get(j);
                if (sumToIndex.containsKey(sum)) {
                    int[] first = sumToIndex.get(sum);
                    int a2 = first[0], b2 = first[1], c2 = i, d2 = j;
                    final List<Integer> tempMinimumList = new ArrayList<>(Arrays.asList(a2, b2, c2, d2));
                    if (validMinimumIndex(a2, b2, c2, d2) && (resultList.isEmpty() || isMinimum(tempMinimumList, resultList))) {
                        resultList = tempMinimumList;
                    }
                } else {
                    sumToIndex.put(sum, new int[] {i, j});
                }
            }
        }
        return resultList;
    }

    private boolean validMinimumIndex(int a, int b, int c, int d) {
        // validate index;
        if ( a >= c || b == c || b == d || b < a || d < c )
            return false;

        return true;
    }

    private boolean isMinimum(List<Integer> list, List<Integer> resultList) {
        return list.get(0) < resultList.get(0) || (list.get(0) == resultList.get(0)  && list.get(1) < resultList.get(1));
    }

}
