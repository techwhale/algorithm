package org.practise.algorithm.interviewbit.dynamicprogramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Find out the number of N digit numbers, whose digits on being added equals to a given number S.
 * Note that a valid number starts from digits 1-9 except the number 0 itself. i.e. leading zeroes are not allowed.
 *
 * Since the answer can be large, output answer modulo 1_000_000_007
 *
 * **
 *
 * N = 2, S = 4
 * Valid numbers are {22, 31, 13, 40}
 * Hence output 4.
 */
public class NDigitNumbersWithDigitSumS {
    private static final int MODULO = 1_000_000_007;
    public int solve(int N, int sum) {
        Map<DigitSum, Long> memo = new HashMap<>();
        return (int) (solve(0, N, 0, sum, memo) % MODULO);
    }

    private long solve(int position, int N, int sum, int target,  Map<DigitSum, Long> memo) {
        final DigitSum key = new DigitSum(position, sum);
        if (memo.containsKey(key)) return memo.get(key);
        if (position == N) {
            if (sum == target) return 1;
            else return 0;
        }

        if (sum > target) return 0;
        int start = position == 0 ? 1 : 0;
        long answer = 0;
        for (int i = start; i <= 9; i++) {
            if (sum + i <= target && position + 1 <= N)
                answer += solve(position + 1, N, sum + i, target, memo) % MODULO;
        }
        memo.put(key, answer);
        return answer;
    }

    private class DigitSum {
        int position;
        int sum;

        public DigitSum(int position, int sum){
            this.position = position;
            this.sum = sum;
        }

        @Override
        public int hashCode() {
            return position * 31 + sum;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (this.getClass() != obj.getClass()) return false;
            DigitSum that = (DigitSum) obj;
            return this.position == that.position && this.sum == that.sum;
        }
    }
}
