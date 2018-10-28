package org.practise.algorithm.interviewbit.easy;

import java.util.List;

public class SortColor {
    public void sortColors(List<Integer> a) {
        int[] colorCount = new int[3];
        for (int color : a) {
            colorCount[color]++;
        }
        int index = 0;
        for (int color = 0; color < 3; color++) {
            if (colorCount[color] < 1) continue;
            for (int j = 0; j < colorCount[color]; j++) {
                a.set(index++, color);
            }
        }
    }
}
