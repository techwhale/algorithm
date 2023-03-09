package org.practise.algorithm.leetcode.string.medium;

import java.util.List;
/**
 * 524. Longest Word in Dictionary through Deleting
 * Medium
 * Given a string s and a string array dictionary, return the longest string in the dictionary that can be formed by deleting some of the given string characters. If there is more than one possible result, return the longest word with the smallest lexicographical order. If there is no possible result, return the empty string.
 *
 * Example 1:
 *
 * Input: s = "abpcplea", dictionary = ["ale","apple","monkey","plea"]
 * Output: "apple"
 * Example 2:
 *
 * Input: s = "abpcplea", dictionary = ["a","b","c"]
 * Output: "a"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * 1 <= dictionary.length <= 1000
 * 1 <= dictionary[i].length <= 1000
 * s and dictionary[i] consist of lowercase English letters.
 */
public class LongestWordInDictionaryViaDeleting {
    public String findLongestWord(String input, List<String> dictionary) {
        String maxWord = "";
        for (String word : dictionary) {
            if (canFormWord(input, word)) {
                if (word.length() > maxWord.length() || (word.length() == maxWord.length() && word.compareTo(maxWord) < 0)) {
                    maxWord = word;
                }
            }
        }
        return maxWord;
    }

    private boolean canFormWord(String input, String word) {
        int left = 0, right = 0;
        int charLength = 0;
        while (left < input.length() && right < word.length()) {
            if (input.charAt(left) == word.charAt(right)) {
                left++;
                right++;
                charLength++;
            } else {
                left++;
            }
        }
        return charLength == word.length();
    }
}
