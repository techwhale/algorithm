package org.practise.algorithm.leetcode.string.medium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest transformation sequence from beginWord to endWord, such that:
 *
 * Only one letter can be changed at a time.
 * Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
 * Note:
 *
 * Return 0 if there is no such transformation sequence.
 * All words have the same length.
 * All words contain only lowercase alphabetic characters.
 * You may assume no duplicates in the word list.
 * You may assume beginWord and endWord are non-empty and are not the same.
 * Example 1:
 *
 * Input:
 * beginWord = "hit",
 * endWord = "cog",
 * wordList = ["hot","dot","dog","lot","log","cog"]
 *
 * Output: 5
 *
 * Explanation: As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
 * return its length 5.
 * Example 2:
 *
 * Input:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 *
 * Output: 0
 *
 * Explanation: The endWord "cog" is not in wordList, therefore no possible transformation.
 */
public class WordLadder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {

        Map<String, List<String>> mapOneDiffCharacter = new HashMap<>();

        for (int i = 0; i <= wordList.size(); i++) {
            String word1 = i == wordList.size() ? beginWord : wordList.get(i);
            for (int j = 0; j < wordList.size(); j++) {

                if (i == j) continue;

                String word2 = wordList.get(j);

                if (isExactlyOneCharDifferent(word1, word2)) {
                    List<String> transformationList = mapOneDiffCharacter.getOrDefault(word1, new ArrayList<>());
                    transformationList.add(word2);
                    mapOneDiffCharacter.putIfAbsent(word1, transformationList);
                }
            }
        }

        Map<String, Long> memo = new HashMap<>();

        long transformationLength = minTransformationLength(beginWord, endWord, mapOneDiffCharacter, memo);
        return transformationLength == Long.MAX_VALUE ? 0 : (int) transformationLength;
    }

    private long minTransformationLength(String start, String target, Map<String, List<String>> mapOneDiffCharacter, Map<String, Long> memo) {
        long transformationLength = Long.MAX_VALUE;
        if (memo.containsKey(start))
            return memo.get(start);
        else if (start.equals(target))
            return 1;
        else if (! mapOneDiffCharacter.containsKey(start))
            return transformationLength;
        else {
            List<String> transformationList = mapOneDiffCharacter.get(start);
            mapOneDiffCharacter.remove(start);
            for (String word : transformationList) {
                long length = minTransformationLength(word, target, mapOneDiffCharacter, memo);
                if (length == Long.MAX_VALUE) continue;
                transformationLength = Math.min(transformationLength, length + 1);
            }
            memo.put(start, transformationLength);
        }
        return transformationLength;
    }

    private boolean isExactlyOneCharDifferent(String word1, String word2) {
        int difference = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i))
                difference++;
            if (difference > 1) break;
        }
        return difference == 1;
    }
}
