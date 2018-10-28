package org.practise.algorithm.interviewbit.easy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diffk {
    public int diffPossible(List<Integer> A, int B) {
        Map<Integer, Integer> m = new HashMap();
        for (int i = 0; i < A.size(); i++) {
            final int elem = A.get(i);
            if (m.containsKey(elem - B)) {
                return 1;
            }
            m.putIfAbsent(elem, i);
        }
        return 0;
    }
}
