package org.practise.algorithm.leetcode.contest;

import java.util.HashMap;
import java.util.Map;

public class XKindOfCard {
    public boolean hasGroupsSizeX(int[] deck) {
        if (deck == null || deck.length < 2) {
            return false;
        }
        Map<Integer, Integer> uniqueCount = new HashMap<>();
        for (int card : deck) {
            uniqueCount.put(card, uniqueCount.getOrDefault(card, 0) + 1);
        }
        int numberOfCardSplit = Integer.MIN_VALUE;
        for (Integer key : uniqueCount.keySet()) {
            int cardCount = uniqueCount.get(key);
            if (numberOfCardSplit == Integer.MIN_VALUE) {
                numberOfCardSplit = cardCount;
            }
            numberOfCardSplit = gcd(numberOfCardSplit, cardCount);
            if (numberOfCardSplit < 2) return false;
        }
        return true;
    }

    private int gcd(int a, int b) {
        if (a > b) {
            return gcd(b, a);
        }
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }
}
