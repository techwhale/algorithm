package org.practise.algorithm.interviewbit.easy;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a string,
 * find the length of the longest substring without repeating characters.
 *
 * Example:
 *
 * The longest substring without repeating letters for "abcabcbb" is "abc", which the length is 3.
 *
 * For "bbbbb" the longest substring is "b", with the length of 1.
 */
public class LongestSubstringWithoutRepeat {
    public int lengthOfLongestSubstring(String word) {
        if (word == null || word.isEmpty()) return 0;
        Map<Character, Integer> characterIndexMap = new HashMap<>();
        int longest = Integer.MIN_VALUE, lastFoundIndex = 0;
        for (int i = 1; i <= word.length(); i++) {
            char ch = word.charAt(i - 1);
            if (characterIndexMap.containsKey(ch)) {
                lastFoundIndex = Math.max(lastFoundIndex, characterIndexMap.get(ch));
            }
            longest = Math.max(i - lastFoundIndex, longest);
            characterIndexMap.put(ch, i);
        }
        return longest;
    }
}
