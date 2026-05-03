package org.practise.algorithm.leetcode.design.hard;

import java.util.Map;
import java.util.TreeMap;

/**
 * 2276. Count Integers in Intervals
 * Attempted
 * Hard
 * Topics
 * Companies
 * Hint
 * Given an empty set of intervals, implement a data structure that can:
 *
 * Add an interval to the set of intervals.
 * Count the number of integers that are present in at least one interval.
 * Implement the CountIntervals class:
 *
 * CountIntervals() Initializes the object with an empty set of intervals.
 * void add(int left, int right) Adds the interval [left, right] to the set of intervals.
 * int count() Returns the number of integers that are present in at least one interval.
 * Note that an interval [left, right] denotes all the integers x where left <= x <= right.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["CountIntervals", "add", "add", "count", "add", "count"]
 * [[], [2, 3], [7, 10], [], [5, 8], []]
 * Output
 * [null, null, null, 6, null, 8]
 *
 * Explanation
 * CountIntervals countIntervals = new CountIntervals(); // initialize the object with an empty set of intervals.
 * countIntervals.add(2, 3);  // add [2, 3] to the set of intervals.
 * countIntervals.add(7, 10); // add [7, 10] to the set of intervals.
 * countIntervals.count();    // return 6
 *                            // the integers 2 and 3 are present in the interval [2, 3].
 *                            // the integers 7, 8, 9, and 10 are present in the interval [7, 10].
 * countIntervals.add(5, 8);  // add [5, 8] to the set of intervals.
 * countIntervals.count();    // return 8
 *                            // the integers 2 and 3 are present in the interval [2, 3].
 *                            // the integers 5 and 6 are present in the interval [5, 8].
 *                            // the integers 7 and 8 are present in the intervals [5, 8] and [7, 10].
 *                            // the integers 9 and 10 are present in the interval [7, 10].
 *
 *
 * Constraints:
 *
 * 1 <= left <= right <= 109
 * At most 105 calls in total will be made to add and count.
 * At least one call will be made to count
 */
public class CountIntegerInIntervals {
    private int count;
    private TreeMap<Integer, Integer> map;

    public CountIntegerInIntervals() {
        this.count = 0;
        this.map = new TreeMap<>();
    }

    public void add(int left, int right) {
        Map.Entry<Integer, Integer> it = map.floorEntry(left);
        if (it == null || it.getValue() < left) {
            it = map.higherEntry(left);
        }
        for (; it!= null && it.getKey() <= right; it =  map.higherEntry(left)) {
            left = Math.min(left, it.getKey());
            right = Math.max(right, it.getValue());
            count = count - (it.getValue() - it.getKey() + 1);
             map.remove(it.getKey());
        }
        map.put(left, right);
        count += (right - left + 1);
    }

    public int count() {
        return count;
    }
}
