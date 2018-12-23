package org.practise.algorithm.leetcode.array.medium;

import org.testng.annotations.Test;

public class PrisonCellsAfterNDaysTest {
    private PrisonCellsAfterNDays obj = new PrisonCellsAfterNDays();

    @Test
    public void testPrisonCellsAfterNDays() {
        int N = 1000000000;
        int[] cells = new int[] {1,0,0,1,0,0,1,0};
        obj.prisonAfterNDays(cells, N);
    }
}