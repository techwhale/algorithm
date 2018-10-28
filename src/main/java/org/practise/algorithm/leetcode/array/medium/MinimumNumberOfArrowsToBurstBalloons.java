package org.practise.algorithm.leetcode.array.medium;

import java.util.*;

/**
 * There are a number of spherical balloons spread in two-dimensional space. For each balloon, provided input is the start and end coordinates
 * of the horizontal diameter. Since it's horizontal, y-coordinates don't matter and hence the x-coordinates of start and end of the diameter suffice.
 * Start is always smaller than end. There will be at most 104 balloons.
 *
 * An arrow can be shot up exactly vertically from different points along the x-axis. A balloon with xstart and xend bursts
 * by an arrow shot at x if xstart ≤ x ≤ xend. There is no limit to the number of arrows that can be shot. An arrow once shot keeps travelling up infinitely.
 * The problem is to find the minimum number of arrows that must be shot to burst all balloons.
 *
 * Example:
 *
 * Input:
 * [[10,16], [2,8], [1,6], [7,12]]
 *
 * Output:
 * 2
 *
 * Explanation:
 * One way is to shoot one arrow for example at x = 6 (bursting the balloons [2,8] and [1,6]) and another arrow at x = 11 (bursting the other two balloons).
 */
public class MinimumNumberOfArrowsToBurstBalloons {
    public int findMinArrowShots(int[][] points) {
        if (points == null || points.length < 1 || points[0] == null || points[0].length < 1) {
            return 0;
        }
        Arrays.sort(points, new NarrowRangeFirstSorter());
        long arrowsNeeded = 0, lastRightMax = Long.MIN_VALUE;
        for (int i = 0; i < points.length; i++) {
            int[] point = points[i];
            if (lastRightMax < point[0]) {
                arrowsNeeded++;
                lastRightMax = point[1];
            }
        }
        return  (int) arrowsNeeded;
    }

    class NarrowRangeFirstSorter implements Comparator<int[]> {
        public int compare(int[] a, int[] b) {
            return  a[1] - b[1];
        }
    }
}
