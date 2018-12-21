package org.practise.algorithm.leetcode.array.medium;

import java.util.HashMap;
import java.util.Map;

/**
 * There are 8 prison cells in a row, and each cell is either occupied or vacant.
 *
 * Each day, whether the cell is occupied or vacant changes according to the following rules:
 *
 * If a cell has two adjacent neighbors that are both occupied or both vacant, then the cell becomes occupied.
 * Otherwise, it becomes vacant.
 * (Note that because the prison is a row, the first and the last cells in the row can't have two adjacent neighbors.)
 *
 * We describe the current state of the prison in the following way: cells[i] == 1 if the i-th cell is occupied, else cells[i] == 0.
 *
 * Given the initial state of the prison, return the state of the prison after N days (and N such changes described above.)
 *
 *
 *
 * Example 1:
 *
 * Input: cells = [0,1,0,1,1,0,0,1], N = 7
 * Output: [0,0,1,1,0,0,0,0]
 * Explanation:
 * The following table summarizes the state of the prison on each day:
 * Day 0: [0, 1, 0, 1, 1, 0, 0, 1]
 * Day 1: [0, 1, 1, 0, 0, 0, 0, 0]
 * Day 2: [0, 0, 0, 0, 1, 1, 1, 0]
 * Day 3: [0, 1, 1, 0, 0, 1, 0, 0]
 * Day 4: [0, 0, 0, 0, 0, 1, 0, 0]
 * Day 5: [0, 1, 1, 1, 0, 1, 0, 0]
 * Day 6: [0, 0, 1, 0, 1, 1, 0, 0]
 * Day 7: [0, 0, 1, 1, 0, 0, 0, 0]
 *
 * Example 2:
 *
 * Input: cells = [1,0,0,1,0,0,1,0], N = 1000000000
 * Output: [0,0,1,1,1,1,1,0]
 *
 *
 * Note:
 *
 * cells.length == 8
 * cells[i] is in {0, 1}
 * 1 <= N <= 10^9
 */
public class PrisonCellsAfterNDays {
    public int[] prisonAfterNDays(int[] cells, int N) {
        Map<Integer, Integer> seen = new HashMap<>();

        int state = convertToState(cells);

        while (N > 0) {
            if (seen.containsKey(state)) {
                N %= seen.get(state) - N;
            }
            seen.put(state, N);

            if (N > 0) {
                state = nextDay(state);
                N--;
            }
        }

        return convertToCell(state);
    }

    private int nextDay(int state) {
        int result = 0;
        for (int i = 1; i <= 6; i++) {
            if (((state >> (i - 1)) & 1) == ((state >> (i + 1)) & 1)) {
                result |= 1 << i;
            }
        }
        return result;
    }

    private int[] convertToCell(int state) {
        int[] cells = new int[8];
        for (int i = 0; i < 8; i++) {
            if (((state >> i) & 1) > 0)
                cells[i] = 1;
        }
        return cells;
    }

    private int convertToState(int[] cells) {
        int state = 0;
        for (int i = 0; i < 8; i++) {
            if (cells[i] > 0)
                state ^= 1 << i;
        }
        return state;
    }
}
