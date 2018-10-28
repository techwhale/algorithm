package org.practise.algorithm.interviewbit.easy;

import java.util.ArrayList;
import java.util.List;

public class RemoveDuplicatesII {
    public int removeDuplicates(ArrayList<Integer> a) {
        int index = 2;

        Integer previousStore = null;
        // do shift elements at first
        for (int i = 2; i < a.size(); i++) {
            if (a.get(i).intValue() == a.get(i - 2).intValue() && a.get(i).intValue() == a.get(i - 1).intValue()) {
                // skip
            } else {
                int temp = a.get(i);
                if (previousStore != null) a.set(index - 1, previousStore);
                previousStore = temp;
                index++;
            }
        }

        if (previousStore != null) {
            a.set(index - 1, previousStore);
        }

        // remove element from last
        for (int i = a.size() - 1; i >= index; i--)
            a.remove(i);

        return a.size();
    }

    public int minimize(final List<Integer> A, final List<Integer> B, final List<Integer> C) {
        int minResult = Integer.MAX_VALUE, aPtr = 0, bPtr = 0, cPtr = 0;
        while (aPtr < A.size() && bPtr < B.size() && cPtr < C.size()) {
            int aVal = A.get(aPtr), bVal = B.get(bPtr), cVal = C.get(cPtr);
            int min = Math.min(aVal, Math.min(bVal, cVal));
            int result = Math.max(Math.abs(aVal - bVal),
                    Math.max(Math.abs(bVal - cVal), Math.abs(cVal - aVal)));
            minResult = Math.min(minResult, result);

            if (min == aVal) {
                aPtr++;
            } else if (min == bVal) {
                bPtr++;
            } else {
                cPtr++;
            }
        }
        return minResult;
    }
}
