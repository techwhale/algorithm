package org.practise.algorithm.leetcode.array.medium;

import java.util.*;

public class PartitionDisjointed {
    public int partitionDisjoint(int[] A) {
        List<int[]> list = new ArrayList<int[]>();
        for (int i = 0; i < A.length; i++) {
            list.add(new int[] {A[i], i});
        }
        Collections.sort(list, new MyComparator());
        Set<Integer> addedIndex = new HashSet<Integer>();
        Iterator<int[]> iterator = list.iterator();
        int[] next = iterator.next();
        int maxIndex = next[1] + 1;
        addedIndex.add(next[1]);
        while (maxIndex != addedIndex.size()) {
            int[] lowestElement = iterator.next();
            maxIndex = Math.max(maxIndex, lowestElement[1] + 1);
            addedIndex.add(lowestElement[1]);
        }
        return addedIndex.size();
    }

    class MyComparator implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return a[1] - b[1];
            }
        }
    }
}
