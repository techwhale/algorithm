package org.practise.algorithm.geekforgeek.bitmanipulation;

/**
 * Equal Sum and XOR
 * Given a positive integer n, find count of positive integers i such that 0 <= i <= n and n+i = n^i
 * Examples :
 *
 * Input  : n = 7
 * Output : 1
 * Explanation:
 * 7^i = 7+i holds only for only for i = 0
 * 7+0 = 7^0 = 7
 *
 *
 * Input n = 12
 * Output: 4
 * 12^i = 12+i hold only for i = 0, 1, 2, 3
 * for i=0, 12+0 = 12^0 = 12
 * for i=1, 12+1 = 12^1 = 13
 * for i=2, 12+2 = 12^2 = 14
 * for i=3, 12+3 = 12^3 = 15
 */
public class EqualSumAndXOR {
    public int countPossibility(int n) {
        int digits = 0;
        while (n > 0) {
            digits++;
            n = (n & (n - 1));
        }
        return  (int) Math.pow(2, digits);
    }
}
