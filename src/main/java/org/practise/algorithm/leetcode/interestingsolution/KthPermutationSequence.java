package org.practise.algorithm.leetcode.interestingsolution;

import java.util.ArrayList;
import java.util.List;

/**
 * The set [1,2,3,…,n] contains a total of n! unique permutations.
 *
 * By listing and labeling all of the permutations in order,
 * We get the following sequence (ie, for n = 3 ) :
 *
 * 1. "123"
 * 2. "132"
 * 3. "213"
 * 4. "231"
 * 5. "312"
 * 6. "321"
 * Given n and k, return the kth permutation sequence.
 *
 * For example, given n = 3, k = 4, ans = "231"
 *
 *  Good questions to ask the interviewer :
 * 1. What if n is greater than 10. How should multiple digit numbers be represented in string?
 *  In this case, just concatenate the number to the answer.
 * so if n = 11, k = 1, ans = "1234567891011"
 *
 * 2. Whats the maximum value of n and k?
 *  In this case, k will be a positive integer thats less than INT_MAX.
 */
public class KthPermutationSequence {
//    public String getPermutation(int n, int k) {
//        List<Integer> numberList = new ArrayList<>();
//        int[] factorial = new int[n + 1];
//        factorial[0] = 1;
//        for (int i = 1; i <= n; i++) {
//            numberList.add(i);
//            factorial[i] = n > 12 ? Integer.MAX_VALUE : factorial[i - 1] * i;
//        }
//
//        k--;
//
//        StringBuilder builder = new StringBuilder();
//        for (int i = 1; i <= n; i++) {
//            int index = k / factorial[n - i];
//            builder.append(numberList.get(index));
//            numberList.remove(index);
//            k -= index * factorial[n - i];
//        }
//
//        return builder.toString();
//    }


    public String getPermutation(int n, int k) {
        ArrayList<Integer> candidateSet = new ArrayList<>();
        for (int i = 1; i <= n; i++){
            candidateSet.add(i);
        }
        return getPermutation(k - 1, candidateSet);
    }

    private static String getPermutation(int k, ArrayList<Integer> candidateSet) {
        int n = candidateSet.size();
        if (n == 0) {
            return "";
        }
        if (k > factorial(n)) return ""; // invalid. Should never reach here.

        int f = factorial(n - 1);
        int pos = k / f;
        k %= f;
        String ch = String.valueOf(candidateSet.get(pos));
        // now remove the character ch from candidateSet.
        candidateSet.remove(pos);
        return ch + getPermutation(k, candidateSet);
    }

    static int factorial(int n) {
        if (n > 12) {
            // this overflows in int. So, its definitely greater than k
            // which is all we care about. So, we return INT_MAX which
            // is also greater than k.
            return Integer.MAX_VALUE;
        }
        // Can also store these values. But this is just < 12 iteration, so meh!
        int fact = 1;
        for (int i = 2; i <= n; i++) fact *= i;
        return fact;
    }
}
