package org.practise.algorithm.leetcode.contest;

import java.util.*;

public class ReorderLogFiles {
    public String[] reorderLogFiles(String[] logs) {
        int n = logs.length;
        String[][] ss = new String[n][];
        for (int i = 0; i < n; i++) {
            int ind = logs[i].indexOf(" ");
            String id = logs[i].substring(0, ind);
            String rem = logs[i].substring(ind + 1);
            ss[i] = new String[]{rem, id, logs[i], String.format("%05d", i)};
        }
        Arrays.sort(ss, new Comparator<String[]>() {
            public int compare(String[] a, String[] b) {
                boolean ad = a[0].charAt(0) >= '0' && a[0].charAt(0) <= '9';
                boolean bd = b[0].charAt(0) >= '0' && b[0].charAt(0) <= '9';
                if (!ad && bd) return -1;
                if (ad && !bd) return 1;
                if (!ad && !bd) {
                    if (!a[0].equals(b[0])) return a[0].compareTo(b[0]);
                    return a[1].compareTo(b[1]);
                } else {
                    return a[3].compareTo(b[3]);
                }
            }
        });
        String[] ret = new String[n];
        for (int i = 0; i < n; i++) {
            ret[i] = ss[i][2];
        }
        return ret;
    }
}
