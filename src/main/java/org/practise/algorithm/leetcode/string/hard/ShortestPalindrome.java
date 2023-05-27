package org.practise.algorithm.leetcode.string.hard;

/**
 * 214. Shortest Palindrome
 * Hard
 * 3.1K
 * 219
 * company
 * Uber
 * company
 * Bloomberg
 * company
 * Microsoft
 * You are given a string s. You can convert s to a
 * palindrome
 *  by adding characters in front of it.
 *
 * Return the shortest palindrome you can find by performing this transformation.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aacecaaa"
 * Output: "aaacecaaa"
 * Example 2:
 *
 * Input: s = "abcd"
 * Output: "dcbabcd"
 *
 *
 * Constraints:
 *
 * 0 <= s.length <= 5 * 104
 * s consists of lowercase English letters only.
 */
public class ShortestPalindrome {
    public String shortestPalindrome(String s) {
        String reverse = new StringBuilder(s).reverse().toString();
        String newString = new StringBuilder(s).append("#").append(reverse).toString();
        int[] prefix = new int[newString.length()];

        for (int i = 1; i < newString.length(); i++) {
            int j = prefix[i - 1];
            while (j > 0 && newString.charAt(i) != newString.charAt(j)) {
                j = prefix[j - 1];
            }

            if (newString.charAt(i) == newString.charAt(j)) {
                j++;
            }
            prefix[i] = j;
        }
        return reverse.substring(0, s.length() - prefix[newString.length() - 1]) + s;
    }
}
