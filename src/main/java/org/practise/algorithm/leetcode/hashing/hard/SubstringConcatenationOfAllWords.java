package org.practise.algorithm.leetcode.hashing.hard;

import java.util.*;

/**
 * You are given a string, s, and a list of words, words, that are all of the same length.
 * Find all starting indices of substring(s) in s that is a concatenation of each word in words exactly once and without any intervening characters.
 *
 * Example 1:
 *
 * Input:
 *   s = "barfoothefoobarman",
 *   words = ["foo","bar"]
 * Output: [0,9]
 * Explanation: Substrings starting at index 0 and 9 are "barfoor" and "foobar" respectively.
 * The output order does not matter, returning [9,0] is fine too.
 * Example 2:
 *
 * Input:
 *   s = "wordgoodstudentgoodword",
 *   words = ["word","student"]
 * Output: []
 */
public class SubstringConcatenationOfAllWords {

    public List<Integer> findSubstring(String s, String[] words) {
        if (s == null || s.isEmpty() || words == null || words.length == 0)  {
            return Collections.EMPTY_LIST;
        }
        int wordLength = words[0].length(), stringLength = s.length(), N = words.length, totalWordLength = wordLength * N;
        if (totalWordLength > stringLength)
            return Collections.EMPTY_LIST;

        Map<String, Integer> map = new HashMap<>();
        for (String word: words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i <= stringLength - totalWordLength; i++) {
            Map<String, Integer> tempMap = new HashMap<>(map);
            int j = i;

            while (j < i + totalWordLength) {
                String word = s.substring(j, j + wordLength);
                if (!tempMap.containsKey(word)) {
                    break;
                }
                tempMap.put(word, tempMap.get(word) - 1);
                j = j + wordLength;
            }

            int count = 0;
            Iterator<Map.Entry<String, Integer>> iterator = tempMap.entrySet().iterator();
            while (iterator.hasNext() && count == 0) {
                Map.Entry<String, Integer> next = iterator.next();
                if (next.getValue() > 0) {
                    count++;
                }
            }

            if (count == 0)
                resultList.add(i) ;

        }

        return resultList;
    }

//    public List<Integer> findSubstring(String S, String[] words) {
//        // Number of a characters of a word in list L.
//        int size_word = words[0].length();
//
//        // Number of words present inside list L.
//        int word_count = words.length;
//
//        // Total characters present in list L.
//        int size_L = size_word * word_count;
//
//        // Resultant vector which stores indices.
//        List<Integer> result = new ArrayList<>();
//
//        // If the total number of characters in list L
//        // is more than length of string S itself.
//        if (size_L > S.length())
//            return result;
//
//        // Map stores the words present in list L
//        // against it's occurrences inside list L
//        Map<String, Integer> map = new HashMap<>();
//        for (int i = 0; i < word_count; i++)
//            map.put(words[i], map.getOrDefault(words[i], 0) + 1);
//
//        for (int i = 0; i <= S.length() - size_L; i++) {
//            Map<String, Integer> temp_map = new HashMap<>(map);
//
//
//            int j = i;
//
//            // Traverse the substring
//            while (j < i + size_L) {
//
//                // Extract the word
//                String word = S.substring(j, j + size_word);
//
//                // If word not found simply break.
//                if (! map.containsKey(word))
//                    break;
//
//                    // Else decrement the count of word from hash_map
//                else
//                    temp_map.put(word, temp_map.get(word) - 1);
//
//                j += size_word;
//            }
//
//            int count = 0;
//            final Iterator<Map.Entry<String, Integer>> iterator = temp_map.entrySet().iterator();
//            while (iterator.hasNext()) {
//                final Map.Entry<String, Integer> next = iterator.next();
//                if (next.getValue() > 0) {
//                    count++;
//                }
//            }
//
//            // Store the starting index of that substring
//            if (count == 0)
//                result.add(i);
//        }
//
//        return result;
//    }
}
