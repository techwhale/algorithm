package org.practise.algorithm.leetcode.combination;

import java.util.ArrayList;
import java.util.List;

/**
 * 131. Palindrome Partitioning
 * Medium
 * Given a string s, partition s such that every substring of the partition is a
 * palindrome. Return all possible palindrome partitioning of s.
 *
 * Example 1:
 *
 * Input: s = "aab"
 * Output: [["a","a","b"],["aa","b"]]
 * Example 2:
 *
 * Input: s = "a"
 * Output: [["a"]]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 16
 * s contains only lowercase English letters.
 *
 */

public class PalindromePartitioningI {
    public List<List<String>> partition(String s) {
        List<List<String>> allCombinations = new ArrayList<>();
        allCombinations(s, 0, new ArrayList<>(), allCombinations);

        List<List<String>> result = new ArrayList<>();
        for (List<String> combination : allCombinations) {
            if(isPalindrome(combination)) {
                result.add(combination);
            }
        }
        return result;
    }

    private boolean isPalindrome(List<String> words) {
        for (String word : words) {
            if (! isPalindrome(word)) {
                return false;
            }
        }
        return true;
    }

    private boolean isPalindrome(String word) {
        for (int startIdx = 0, endIdx = word.length() -1; startIdx < endIdx; startIdx++, endIdx--) {
            if (word.charAt(startIdx) != word.charAt(endIdx)) {
                return false;
            }
        }
        return true;
    }

    private void allCombinations(String s, int startIndex, List<String> currentCombinations, List<List<String>> allCombinations) {
        if (startIndex == s.length()) {
            allCombinations.add(new ArrayList<>(currentCombinations));
            return;
        }

        for (int i = 1; i < (s.length() - startIndex) + 1; i++) {
            String word = s.substring(startIndex, startIndex + i);
            currentCombinations.add(word);
            allCombinations(s, startIndex + i, currentCombinations, allCombinations);
            currentCombinations.remove(currentCombinations.size() - 1);
        }
    }
}