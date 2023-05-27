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
    public String alienOrder(String[] words) {
        Map<Character, Integer> counts = new HashMap<Character, Integer>();
        Map<Character, List<Character>> adjList = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                counts.put(c, 0);
                adjList.put(c, new ArrayList<>());
            }
        }

        for (int i = 0; i < words.length - 1; i++) {
            String curr = words[i];
            String next = words[i + 1];

            if (curr.length() > next.length() && curr.startsWith(next)){
                return "";
            }
            for (int j = 0; j < Math.min(curr.length(), next.length()); j++) {
                if (curr.charAt(j) != next.charAt(j)){
                    adjList.get(curr.charAt(j)).add(next.charAt(j));
                    counts.put(next.charAt(j), counts.get(next.charAt(j)) + 1);
                    break;
                }
            }
        }

        Queue<Character> queue = new LinkedList<>();
        for (char c : counts.keySet()) {
            if (counts.get(c).equals(0)) {
                queue.offer(c);
            }
        }

        StringBuilder builder = new StringBuilder();
        while (!queue.isEmpty()) {
            char c = queue.poll();
            builder.append(c);
            for (char next : adjList.get(c)){
                counts.put(next, counts.get(next) - 1);
                if (counts.get(next).equals(0)) {
                    queue.add(next);
                }
            }
        }
        return builder.length() != counts.size() ? "" :  builder.toString();
    }
}
