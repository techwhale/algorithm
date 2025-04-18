package org.practise.algorithm.leetcode.array.hard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 149. Max Points on a Line
 * Hard
 * Topics
 * Companies
 * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane, return the maximum number of points that lie on the same straight line.
 *
 * Example 1:
 *
 * Input: points = [[1,1],[2,2],[3,3]]
 * Output: 3
 *
 * Example 2:
 * Input: points = [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]
 * Output: 4
 *
 * Constraints:
 * 1 <= points.length <= 300
 * points[i].length == 2
 * -104 <= xi, yi <= 104
 * All the points are unique.
 */
public class MaxPointOnALine {
    public int maxPoints(int[][] points) {
        if (points.length == 1) {
            return 1;
        }
        int result = 2;
        for (int i = 0; i < points.length; i++) {
            Map<Double, Integer> count = new HashMap<>();
            for (int j = 0; j < points.length; j++) {
                if (i == j) {
                    continue;
                }
                count.merge(
                        Math.atan2(
                                points[j][1] - points[i][1],
                                points[j][0] - points[i][0]
                        ),
                        1,
                        Integer::sum
                );
            }
            result = Math.max(result, Collections.max(count.values()) + 1);
        }
        return result;
    }
}
