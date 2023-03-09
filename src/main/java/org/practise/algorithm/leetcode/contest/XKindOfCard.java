package org.practise.algorithm.leetcode.contest;

import java.util.HashMap;
import java.util.Map;

/**
 * 914. X of a Kind in a Deck of Cards
 * Easy
 *
 * You are given an integer array deck where deck[i] represents the number written on the ith card.
 *
 * Partition the cards into one or more groups such that:
 *
 * Each group has exactly x cards where x > 1, and
 * All the cards in one group have the same integer written on them.
 * Return true if such partition is possible, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: deck = [1,2,3,4,4,3,2,1]
 * Output: true
 * Explanation: Possible partition [1,1],[2,2],[3,3],[4,4].
 * Example 2:
 *
 * Input: deck = [1,1,1,2,2,2,3,3]
 * Output: false
 * Explanation: No possible partition.
 *
 *
 * Constraints:
 *
 * 1 <= deck.length <= 104
 * 0 <= deck[i] < 104
 */
public class XKindOfCard {
    public boolean hasGroupsSizeX(int[] deck) {
        int[] count = new int[10000];
        for (int card : deck)
            count[card]++;

        int g = -1;
        for (int i = 0; i < 10000; i++) {
            if (count[i] == 0) continue;

            if (g == -1) {
                g = count[i];
            } else {
                g = gcd(g, count[i]);
            }
        }
        return g > 1;
    }

    private int gcd(int x, int y) {
        return x == 0 ? y : gcd(y%x, x);
    }
}
