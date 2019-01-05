package org.practise.algorithm.interestingideas;

/**
 * Given an Android 3x3 key lock screen and two integers m and n, where 1 ≤ m ≤ n ≤ 9, count the total number of unlock patterns of the Android lock screen,
 * which consist of minimum of m keys and maximum n keys.
 *
 *
 *
 * Rules for a valid pattern:
 *
 * Each pattern must connect at least m keys and at most n keys.
 * All the keys must be distinct.
 * If the line connecting two consecutive keys in the pattern passes through any other keys, the other keys must have previously selected in the pattern.
 * No jumps through non selected key is allowed.
 * The order of keys used matters.
 *
 *
 *
 *
 *
 * Explanation:
 *
 * | 1 | 2 | 3 |
 * | 4 | 5 | 6 |
 * | 7 | 8 | 9 |
 * Invalid move: 4 - 1 - 3 - 6
 * Line 1 - 3 passes through key 2 which had not been selected in the pattern.
 *
 * Invalid move: 4 - 1 - 9 - 2
 * Line 1 - 9 passes through key 5 which had not been selected in the pattern.
 *
 * Valid move: 2 - 4 - 1 - 3 - 6
 * Line 1 - 3 is valid because it passes through key 2, which had been selected in the pattern
 *
 * Valid move: 6 - 5 - 4 - 1 - 9 - 2
 * Line 1 - 9 is valid because it passes through key 5, which had been selected in the pattern.
 *
 *
 *
 * Example:
 *
 * Input: m = 1, n = 1
 * Output: 9
 */
public class AndroidUnlockPatterns {
    private final int DOTS = 10;
    public int numberOfPatterns(int m, int n) {
        int[][] jumps = new int[DOTS][DOTS];
        jumps[1][3] = jumps[3][1] = 2;
        jumps[1][7] = jumps[7][1] = 4;
        jumps[7][9] = jumps[9][7] = 8;
        jumps[3][9] = jumps[9][3] = 6;

        jumps[1][9] = jumps[9][1] = jumps[2][8] = jumps[8][2] = jumps[4][6] =
                jumps[6][4] = jumps[3][7] = jumps[7][3] = jumps[4][6] = jumps[6][4] = 5;

        boolean[] visited = new boolean[DOTS];

        int totalPattern = 0;
        for (int i = m; i <= n; i++) {
            totalPattern += DFS(1, jumps, visited, i - 1) * 4;
            totalPattern += DFS(2, jumps, visited, i - 1) * 4;
            totalPattern += DFS(5, jumps, visited, i - 1);
        }
        return totalPattern;
    }


    private int DFS(int currentPosition, int[][] jumps, boolean[] visited, int remainingLength) {
        if (remainingLength < 0)
            return 0;
        if (remainingLength == 0){
            return 1;
        }
        visited[currentPosition] = true;
        int count = 0;

        for (int nextPosition = 1; nextPosition < DOTS; nextPosition++) {
            if (! visited[nextPosition] && (jumps[currentPosition][nextPosition] == 0 || visited[jumps[currentPosition][nextPosition]])) {
                count += DFS(nextPosition, jumps, visited, remainingLength - 1);
            }
        }
        visited[currentPosition] = false;
        return count;
    }
}
