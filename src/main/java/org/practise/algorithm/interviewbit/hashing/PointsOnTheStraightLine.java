package org.practise.algorithm.interviewbit.hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Given n points on a 2D plane, find the maximum number of points that lie on the same straight line.
 *
 * Sample Input :
 *
 * (1, 1)
 * (2, 2)
 * Sample Output :
 *
 * 2
 * You will be give 2 arrays X and Y. Each point is represented by (X[i], Y[i])
 */
public class PointsOnTheStraightLine {
    public int maxPoints(List<Integer> XList, List<Integer> YList) {
        if (XList == null || YList == null || XList.isEmpty() || YList.isEmpty()) {
            return 0;
        }
        if (XList.size() < 2)
            return 1;

        int maxPointsOnLine = 0;
        for (int i = 0; i < XList.size(); i++) {
            int x1 = XList.get(i), y1 = YList.get(i);
            // vertical line with same X axis
            Map<Integer, Integer> verticalCount = new HashMap<>();
            Map<Double, Integer> slopeCount = new HashMap<>();
            for (int j = 0; j < XList.size(); j++) {
                if (i == j) continue;
                int x2 = XList.get(j), y2 = YList.get(j);
                int pointsOnLine = 0;
                if (x1 - x2 == 0) {
                    // when two points in same line, count should start with 2
                    pointsOnLine = verticalCount.getOrDefault(x1, 1) + 1;
                    verticalCount.put(x1, pointsOnLine);
                } else {
                    double slope = (y2 - y1)/ ( (x2 - x1) * 1.0d);
                    pointsOnLine = slopeCount.getOrDefault(slope, 1) + 1;
                    slopeCount.put(slope, pointsOnLine);
                }
                maxPointsOnLine = Math.max(maxPointsOnLine, pointsOnLine);
            }
        }
        return maxPointsOnLine;
    }
}
