package org.practise.algorithm.leetcode.string.hard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Given a pattern and a string str, find if str follows the same pattern.
 *
 * Here follow means a full match, such that there is a bijection between a letter in pattern and a non-empty substring in str.
 *
 * Example 1:
 *
 * Input: pattern = "abab", str = "redblueredblue"
 * Output: true
 * Example 2:
 *
 * Input: pattern = pattern = "aaaa", str = "asdasdasdasd"
 * Output: true
 * Example 3:
 *
 * Input: pattern = "aabb", str = "xyzabcxzyabc"
 * Output: false
 * Notes:
 * You may assume both pattern and str contains only lowercase letters.
 */
public class WordPatternII {
    public boolean wordPatternMatch(String pattern, String str) {
        if (pattern.isEmpty())
            return str.isEmpty();

        return wordPatternMatch(pattern, str, 0, 0, new HashMap<>(), new HashSet<>());
    }

    private boolean wordPatternMatch(String pattern, String str, int pattern_index, int string_index, Map<Character, String> mapPattern, Set<String> processingWords) {
        if (pattern.length() == pattern_index && str.length() == string_index)
            return true;

        if (pattern.length() == pattern_index || str.length() == string_index)
            return false;


        boolean result = false;
        char ch = pattern.charAt(pattern_index);
        if (mapPattern.containsKey(ch)) {
            String memoized_word = mapPattern.get(ch);
            int end_position = string_index + memoized_word.length();
            if (end_position <= str.length() && memoized_word.equals(str.substring(string_index, end_position))) {
                result = wordPatternMatch(pattern, str, pattern_index + 1, end_position, mapPattern, processingWords);
            }
        } else {
            for (int end_index = string_index; end_index < str.length(); end_index++) {
                String word = str.substring(string_index, end_index + 1);
                if (processingWords.contains(word))
                    continue;
                mapPattern.put(ch, word);
                processingWords.add(word);
                if (wordPatternMatch(pattern, str, pattern_index + 1, end_index + 1, mapPattern, processingWords)) {
                    return true;
                }
                mapPattern.remove(ch);
                processingWords.remove(word);
            }
        }
        return result;
    }
}
