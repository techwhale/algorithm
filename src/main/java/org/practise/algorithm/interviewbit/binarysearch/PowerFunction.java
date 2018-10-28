package org.practise.algorithm.interviewbit.binarysearch;

public class PowerFunction {
    public int pow(int x, int n, int d) {
        if (n == 0) return 1 % d;
        long result = 1, base = x;
        while (n > 0) {
            if (n % 2 == 1) {
                result = (result * base) % d;
                n--;
            } else {
                base = (base * base) % d;
                n /= 2;
            }
        }
       return (int) (result < 0 ? (d  + result) : result);
    }
}
