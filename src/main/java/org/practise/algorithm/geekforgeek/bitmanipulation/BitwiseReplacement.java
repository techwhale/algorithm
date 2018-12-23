package org.practise.algorithm.geekforgeek.bitmanipulation;

/**
 * Replace every array element by Bitwise Xor of previous and next element
 * Given an array of integers, replace every element with xor of previous and next elements with following exceptions.
 * a) First element is replaced by sum of first and second.
 * b) Last element is replaced by sum of last and second last.
 *
 * Examples:
 *
 * Input: arr[] = { 2, 3, 4, 5, 6}
 * Output: 1 6 6 2 3
 *
 * We get the following array as {2^3, 2^4, 3^5, 4^6, 5^6}
 *
 * Input: arr[] = { 1, 2, 1, 5}
 * Output: 3, 0, 7, 4
 *
 * We get the following array as {1^2, 1^1, 2^5, 1^5}
 */
public class BitwiseReplacement {
    public void bitwiseReplacement(int[] arr) {
        int prev = arr[0];
        arr[0] = arr[0] ^ arr[1];
        final int N = arr.length;
        for (int i = 1; i < N - 1; i++) {
            int curr = arr[i];
            arr[i] = prev ^ arr[i + 1];
            prev = curr;
        }
        arr[N - 1] = prev ^ arr[N - 1];
    }
}
