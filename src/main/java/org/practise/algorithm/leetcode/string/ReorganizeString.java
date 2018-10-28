package org.practise.algorithm.leetcode.string;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Given a string S, check if the letters can be rearranged so that two characters that are adjacent to each other are not the same.
 *
 * If possible, output any possible result.  If not possible, return the empty string.
 *
 * Example 1:
 *
 * Input: S = "aab"
 * Output: "aba"
 * Example 2:
 *
 * Input: S = "aaab"
 * Output: ""
 * Note:
 *
 * S will consist of lowercase letters and have length in range [1, 500].
 */
public class ReorganizeString {
    public String reorganizeString(String S) {
        if (S == null || S.isEmpty()) return "";
        char[] arrChar = S.toCharArray();

        Map<Character, Integer> mapCharacterCount = new HashMap<Character, Integer>();
        for (char ch : arrChar) {
            mapCharacterCount.put(ch, mapCharacterCount.getOrDefault(ch, 0) + 1);
        }

        StringBuilder builder = new StringBuilder();
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> b[1] - a[1]);

        for (Character ch : mapCharacterCount.keySet()) {
            pq.offer(new int[] {ch, mapCharacterCount.get(ch)});
        }

        while (!pq.isEmpty()) {
            int[] mostAvailableCharacter = pq.poll();
            if (pq.isEmpty() && mostAvailableCharacter[1] > 1) return "";
            builder.append((char)mostAvailableCharacter[0]);
            mostAvailableCharacter[1]--;

            int[] secondAvailableChar = null;
            if (! pq.isEmpty()) {
                int[] secondchar = pq.poll();
                builder.append((char)secondchar[0]);
                secondchar[1]--;
                secondAvailableChar = secondchar;
            }
            if (mostAvailableCharacter[1] > 0) pq.offer(mostAvailableCharacter);
            if (secondAvailableChar != null && secondAvailableChar[1] > 0) pq.offer(secondAvailableChar);

        }
        return builder.toString();
    }
}
