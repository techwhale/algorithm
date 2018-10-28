package org.practise.algorithm.interviewbit.easy;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Find the intersection of two sorted arrays.
 * OR in other words,
 * Given 2 sorted arrays, find all the elements which occur in both the arrays.
 *
 * Example :
 *
 * Input :
 *     A : [1 2 3 3 4 5 6]
 *     B : [3 3 5]
 *
 * Output : [3 3 5]
 *
 * Input :
 *     A : [1 2 3 3 4 5 6]
 *     B : [3 5]
 *
 * Output : [3 5]
 *  NOTE : For the purpose of this problem ( as also conveyed by the sample case ),
 *  assume that elements that appear more than once in both arrays should be included multiple times in the final output.
 */
public class IntersectionOfSortedArrays {
    // DO NOT MODIFY THE LIST. IT IS READ ONLY
    public ArrayList<Integer> intersect(final List<Integer> A, final List<Integer> B) {
        ArrayList<Integer> result = new ArrayList<>();
        if (B == null || B.isEmpty() || A == null || A.isEmpty()) return result;

        for (int aIndex = 0, bIndex = 0; aIndex < A.size() && bIndex < B.size(); ) {
            final int a = A.get(aIndex);
            final int b = B.get(bIndex);
            if (a == b) {
                result.add(A.get(aIndex));
                aIndex++;
                bIndex++;
            } else if (a > b) {
                bIndex++;
            } else {
                aIndex++;
            }
        }
        return result;
    }
}
