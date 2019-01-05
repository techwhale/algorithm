package org.practise.algorithm.leetcode.topologicalsorting;

import java.util.*;

/**
 * 269. Alien Dictionary
 *
 * There is a new alien language which uses the latin alphabet. However, the order among letters are unknown to you.
 * You receive a list of non-empty words from the dictionary, where words are sorted lexicographically by the rules of this new language.
 * Derive the order of letters in this language.
 *
 * Example 1:
 *
 * Input:
 * [
 *   "wrt",
 *   "wrf",
 *   "er",
 *   "ett",
 *   "rftt"
 * ]
 *
 * Output: "wertf"
 * Example 2:
 *
 * Input:
 * [
 *   "z",
 *   "x"
 * ]
 *
 * Output: "zx"
 * Example 3:
 *
 * Input:
 * [
 *   "z",
 *   "x",
 *   "z"
 * ]
 *
 * Output: ""
 *
 * Explanation: The order is invalid, so return "".
 * Note:
 *
 * You may assume all letters are in lowercase.
 * You may assume that if a is a prefix of b, then a must appear before b in the given dictionary.
 * If the order is invalid, return an empty string.
 * There may be multiple valid order of letters, return any one of them is fine.
 */
public class AlienDictionary {
    private final int MAX_CHARACTER = 26;
    public String alienOrder(String[] words) {
        Set<Character> uniqueCharacters = getUniqueCharacters(words);
        List<Prerequisite> prerequisites = generatePrerequisites(words);

        int[][] matrix = new int[MAX_CHARACTER][MAX_CHARACTER];
        int[] indegree = new int[MAX_CHARACTER];

        for (Prerequisite p : prerequisites) {
            if (matrix[p.required - 'a'][p.ch - 'a'] == 0) {
                indegree[p.ch - 'a']++;
            }
            matrix[p.required - 'a'][p.ch - 'a'] = 1;
        }

        String result = "";
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < MAX_CHARACTER; i++) {
            if (uniqueCharacters.contains((char) (i + 'a')) && indegree[i] == 0) {
                queue.offer(i);
            }
        }

        while (! queue.isEmpty()) {
            int c = queue.poll();
            result += (char) (c + 'a');
            for (int i = 0; i < MAX_CHARACTER; i++) {
                if (matrix[c][i] == 1) {
                    if (--indegree[i] == 0) {
                        queue.offer(i);
                    }
                }
            }
        }

        return result.length() == uniqueCharacters.size() ? result : "";
    }

    private List<Prerequisite> generatePrerequisites(String[] words) {
        List<Prerequisite> prerequisiteList = new ArrayList<>();
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            for (int position = 0; position < word.length(); position++) {
                if (position < words[i - 1].length() && word.charAt(position) != words[i - 1].charAt(position)) {
                    prerequisiteList.add(new Prerequisite(word.charAt(position), words[i - 1].charAt(position)));
                    break;
                }
            }
        }
        return prerequisiteList;
    }

    private Set<Character> getUniqueCharacters(String[] words) {
        Set<Character> uniqueCharacters = new HashSet<>();
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                uniqueCharacters.add(word.charAt(i));
            }
        }
        return uniqueCharacters;
    }

    private class Prerequisite {
        char ch;
        char required;
        public Prerequisite(char ch, char required) {
            this.ch = ch;
            this.required = required;
        }
    }
}
