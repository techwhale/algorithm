package org.practise.algorithm.leetcode.memoization.hard;

import java.util.*;

/**
 * Given a non-empty string s and a dictionary wordDict containing a list of non-empty words, add spaces in s to construct a sentence where each word is a valid dictionary word. Return all such possible sentences.
 *
 * Note:
 *
 * The same word in the dictionary may be reused multiple times in the segmentation.
 * You may assume the dictionary does not contain duplicate words.
 * Example 1:
 *
 * Input:
 * s = "catsanddog"
 * wordDict = ["cat", "cats", "and", "sand", "dog"]
 * Output:
 * [
 *   "cats and dog",
 *   "cat sand dog"
 * ]
 * Example 2:
 *
 * Input:
 * s = "pineapplepenapple"
 * wordDict = ["apple", "pen", "applepen", "pine", "pineapple"]
 * Output:
 * [
 *   "pine apple pen apple",
 *   "pineapple pen apple",
 *   "pine applepen apple"
 * ]
 * Explanation: Note that you are allowed to reuse a dictionary word.
 * Example 3:
 *
 * Input:
 * s = "catsandog"
 * wordDict = ["cats", "dog", "sand", "and", "cat"]
 * Output:
 * []
 */
public class WordBreakII {
    public List<String> wordBreak(String s, List<String> B) {
        final HashSet<String> dictionary = new HashSet<>(B);
        Map<Integer,List<String>> mapPositionToWords = new HashMap<>();
        return wordBreak(s, dictionary, mapPositionToWords, 0);
    }

    private List<String> wordBreak(String s, Set<String> dictionary, Map<Integer,List<String>> mapPositionToWords, int start) {
        List<String> resultList = new ArrayList<>();
        if (mapPositionToWords.containsKey(start)) {
            return mapPositionToWords.get(start);
        }

        if (start == s.length()) {
            resultList.add("");
        } else {
            for (int end = start + 1; end <= s.length(); end++) {
                String startWord = s.substring(start, end);
                if (dictionary.contains(startWord)) {
                    List<String> possibleWords = wordBreak(s, dictionary, mapPositionToWords, end);
                    for (String w : possibleWords) {
                        resultList.add(startWord + ((w == "" ? "" : " ") + w));
                    }
                }
            }
        }
        mapPositionToWords.put(start, resultList);
        return resultList;
    }
}
