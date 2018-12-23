package org.practise.algorithm.leetcode.array.medium;

/**
 * Given an array of integers A, a move consists of choosing any A[i], and incrementing it by 1.
 *
 * Return the least number of moves to make every value in A unique.
 *
 *
 *
 * Example 1:
 *
 * Input: [1,2,2]
 * Output: 1
 * Explanation:  After 1 move, the array could be [1, 2, 3].
 * Example 2:
 *
 * Input: [3,2,1,2,1,7]
 * Output: 6
 * Explanation:  After 6 moves, the array could be [3, 4, 1, 2, 5, 7].
 * It can be shown with 5 or less moves that it is impossible for the array to have all unique values.
 *
 *
 * Note:
 *
 * 0 <= A.length <= 40000
 * 0 <= A[i] < 40000
 */
public class MinimumIncrementToMakeArrayUnique {
    private static final int MAX_SIZE = 40001 * 2;
    public int minIncrementForUnique(int[] A) {
        int[] count = new int[MAX_SIZE];
        int N = A.length;
        for (int i = 0; i < N; i++ ) {
            count[A[i]]++;
        }

        int index = 0, duplicate = 0, moves = 0;
        while (index < MAX_SIZE) {
            if (count[index] > 1) {
                duplicate +=  count[index] - 1;
                moves = moves - index * (count[index] - 1);
            } else if (duplicate > 0 && count[index] == 0) {
                duplicate--;
                moves += index;
            }
            index++;
        }
        return moves;
    }
}
