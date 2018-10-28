package org.practise.algorithm.leetcode.interestingsolution;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * In a row of trees, the i-th tree produces fruit with type tree[i].
 *
 * You start at any tree of your choice, then repeatedly perform the following steps:
 *
 * 1. Add one piece of fruit from this tree to your baskets.  If you cannot, stop.
 * 2. Move to the next tree to the right of the current tree.  If there is no tree to the right, stop.
 *
 * Note that you do not have any choice after the initial choice of starting tree: you must perform step 1, then step 2,
 * then back to step 1, then step 2, and so on until you stop.
 *
 * You have two baskets, and each basket can carry any quantity of fruit, but you want each basket to only carry one type of fruit each.
 *
 * What is the total amount of fruit you can collect with this procedure?
 *
 *
 *
 * Example 1:
 *
 * Input: [1,2,1]
 * Output: 3
 * Explanation: We can collect [1,2,1].
 * Example 2:
 *
 * Input: [0,1,2,2]
 * Output: 3
 * Explanation: We can collect [1,2,2].
 * If we started at the first tree, we would only collect [0, 1].
 * Example 3:
 *
 * Input: [1,2,3,2,2]
 * Output: 4
 * Explanation: We can collect [2,3,2,2].
 * If we started at the first tree, we would only collect [1, 2].
 * Example 4:
 *
 * Input: [3,3,3,1,2,1,1,2,3,3,4]
 * Output: 5
 * Explanation: We can collect [1,2,1,1,2].
 * If we started at the first tree or the eighth tree, we would only collect 4 fruits.
 *
 *
 * Note:
 *
 * 1 <= tree.length <= 40000
 * 0 <= tree[i] < tree.length
 */
public class FruitIntoBaskets {
    public int totalFruit(int[] tree) {
        if (tree == null || tree.length < 1) return 0;
        int i = 0, maxFruitCollected = 0;
        Counter count = new Counter();
        for (int j = 0; j < tree.length; j++) {
            count.add(tree[j], 1);
            while (count.size() > 2) {
                int fruit = tree[i];
                count.add(fruit, -1);
                if (count.get(fruit) == 0) {
                    count.remove(fruit);
                }
                i++;
            }
            maxFruitCollected = Math.max(maxFruitCollected, j - i + 1);
        }
        return maxFruitCollected;
    }

    class Counter extends HashMap<Integer, Integer> {
        public int get(int key) {
            return containsKey(key)? super.get(key) : 0;
        }

        public void add(int key, int value) {
            put(key, get(key) + value);
        }
    }
}
