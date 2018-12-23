package org.practise.algorithm.leetcode.hardcorecoding;

/**
 * Given two integers dividend and divisor, divide two integers without using multiplication, division and mod operator.
 *
 * Return the quotient after dividing dividend by divisor.
 *
 * The integer division should truncate toward zero.
 *
 * Example 1:
 *
 * Input: dividend = 10, divisor = 3
 * Output: 3
 * Example 2:
 *
 * Input: dividend = 7, divisor = -3
 * Output: -2
 * Note:
 *
 * Both dividend and divisor will be 32-bit signed integers.
 * The divisor will never be 0.
 * Assume we are dealing with an environment which could only store integers within the 32-bit signed integer range: [−231,  231 − 1].
 * For the purpose of this problem, assume that your function returns 231 − 1 when the division result overflows.
 */
public class DivideTwoIntegers {
    public int divide(int dividend, int divisor) {
        if (divisor == 0)
            return Integer.MAX_VALUE;

        if (dividend == 0 || dividend < divisor)
            return 0;

        int sign = 1;
        if ((dividend < 0 && divisor > 0) || (dividend > 0 && divisor < 0))
            sign = -1;

        final long ldividend = Math.abs((long) dividend), ldivisor = Math.abs((long) divisor);
        final long quotient = ldivide(ldividend, ldivisor);

        if (quotient > Integer.MAX_VALUE) {
            return  (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        return (sign == 1) ? (int) (quotient) : (int) (- quotient);
    }

    private long ldivide(long dividend, long divisor) {
        if (dividend < divisor)
            return 0;

        long multiple = 1, sum = divisor;
        while ((sum + sum) <= dividend) {
            sum += sum;
            multiple += multiple;
        }
        return multiple + ldivide(dividend - sum, divisor);
    }
}
