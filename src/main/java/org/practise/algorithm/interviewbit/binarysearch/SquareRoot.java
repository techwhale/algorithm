package org.practise.algorithm.interviewbit.binarysearch;
// Implement int sqrt(int x).
//
// Compute and return the square root of x.
//
// If x is not a perfect square, return floor(sqrt(x))
// Example :
// Input : 11
// Output : 3
// DO NOT USE SQRT FUNCTION FROM STANDARD LIBRARY
//
public class SquareRoot {
    public int sqrt(int a) {
        if (a <= 1) return a;
        return (int) sqrt(a, 0, a);
    }

    private long sqrt(int val, long low, long high) {
        long mid = 0;
        while (low <= high) {
            mid = (low + high)/2;
            long midSquare = mid * mid;
            if (midSquare == val) {
                return mid;
            } else if (midSquare > val) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return high;
    }
}
