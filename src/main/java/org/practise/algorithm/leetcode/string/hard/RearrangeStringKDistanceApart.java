package org.practise.algorithm.leetcode.string.hard;

import java.util.*;

/**
 * Given a non-empty string s and an integer k, rearrange the string such that the same characters are at least distance k from each other.
 * <p>
 * All input strings are given in lowercase letters. If it is not possible to rearrange the string, return an empty string "".
 * <p>
 * Example 1:
 * <p>
 * Input: s = "aabbcc", k = 3
 * Output: "abcabc"
 * Explanation: The same letters are at least distance 3 from each other.
 * Example 2:
 * <p>
 * Input: s = "aaabc", k = 3
 * Output: ""
 * Explanation: It is not possible to rearrange the string.
 * Example 3:
 * <p>
 * Input: s = "aaadbbcc", k = 2
 * Output: "abacabcd"
 * Explanation: The same letters are at least distance 2 from each other.
 */
public class RearrangeStringKDistanceApart {
    public String rearrangeString(String s, int k) {
        StringBuilder rearrangedString = new StringBuilder();
        Map<Character, Integer> mapCharacterCount = new HashMap<>();
        for (char ch : s.toCharArray()) {
            mapCharacterCount.put(ch, mapCharacterCount.getOrDefault(ch, 0) + 1);
        }

        PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>(new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> a, Map.Entry<Character, Integer> b) {
                return b.getValue()  - a.getValue();
            }
        });

        pq.addAll(mapCharacterCount.entrySet());
        Queue<Map.Entry<Character, Integer>> waitQueue = new LinkedList<>();

        while(! pq.isEmpty()) {
            Map.Entry<Character, Integer> current = pq.poll();
            rearrangedString.append(current.getKey());
            current.setValue(current.getValue() - 1);
            waitQueue.offer(current);

            if (waitQueue.size() < k)  continue;

            Map.Entry<Character, Integer> front = waitQueue.poll();
            if (front.getValue() > 0) {
                pq.offer(front);
            }
        }
        return s.length() == rearrangedString.length() ? rearrangedString.toString() : "";
    }
}
