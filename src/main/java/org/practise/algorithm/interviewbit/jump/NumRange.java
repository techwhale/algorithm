package org.practise.algorithm.interviewbit.jump;

/**
 * Given an array of non negative integers A, and a range (B, C),
 * find the number of continuous subsequences in the array which have sum S in the range [B, C] or B <= S <= C
 *
 * Continuous subsequence is defined as all the numbers A[i], A[i + 1], .... A[j]
 * where 0 <= i <= j < size(A)
 *
 * Example :
 *
 * A : [10, 5, 1, 0, 2]
 * (B, C) : (6, 8)
 * ans = 3
 * as [5, 1], [5, 1, 0], [5, 1, 0, 2] are the only 3 continuous subsequence with their sum in the range [6, 8]
 *
 *  NOTE : The answer is guranteed to fit in a 32 bit signed integer.
 */
public class NumRange {
    public int numRange(int[] A, int B, int C) {
        int sum = 0, count = 0, left = 0, right = 0;
        while (right < A.length) {
            sum += A[right];
            if (B <= sum && sum <= C) {
                count++;
                right++;
            } else if (C < sum) {
                sum = 0;
                left++;
                right = left;
            } else if (sum < B) {
                right++;
            }
            if (right == A.length) {
                left++;
                right = left;
                sum = 0;
            }
        }
        return count;
    }
}
