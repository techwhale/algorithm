package org.practise.algorithm.leetcode.dynamicprogramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 2463. Minimum Total Distance Traveled
 * Hard
 * <p>
 * There are some robots and factories on the X-axis. You are given an integer array robot where robot[i] is the position of the ith robot. You are also given a 2D integer array factory where factory[j] = [positionj, limitj] indicates that positionj is the position of the jth factory and that the jth factory can repair at most limitj robots.
 * <p>
 * The positions of each robot are unique. The positions of each factory are also unique. Note that a robot can be in the same position as a factory initially.
 * <p>
 * All the robots are initially broken; they keep moving in one direction. The direction could be the negative or the positive direction of the X-axis. When a robot reaches a factory that did not reach its limit, the factory repairs the robot, and it stops moving.
 * <p>
 * At any moment, you can set the initial direction of moving for some robot. Your target is to minimize the total distance traveled by all the robots.
 * <p>
 * Return the minimum total distance traveled by all the robots. The test cases are generated such that all the robots can be repaired.
 * <p>
 * Note that
 * <p>
 * All robots move at the same speed.
 * If two robots move in the same direction, they will never collide.
 * If two robots move in opposite directions and they meet at some point, they do not collide. They cross each other.
 * If a robot passes by a factory that reached its limits, it crosses it as if it does not exist.
 * If the robot moved from a position x to a position y, the distance it moved is |y - x|.
 * <p>
 * <p>
 * Example 1:
 * <p>
 * <p>
 * Input: robot = [0,4,6], factory = [[2,2],[6,2]]
 * Output: 4
 * Explanation: As shown in the figure:
 * - The first robot at position 0 moves in the positive direction. It will be repaired at the first factory.
 * - The second robot at position 4 moves in the negative direction. It will be repaired at the first factory.
 * - The third robot at position 6 will be repaired at the second factory. It does not need to move.
 * The limit of the first factory is 2, and it fixed 2 robots.
 * The limit of the second factory is 2, and it fixed 1 robot.
 * The total distance is |2 - 0| + |2 - 4| + |6 - 6| = 4. It can be shown that we cannot achieve a better total distance than 4.
 * Example 2:
 * <p>
 * <p>
 * Input: robot = [1,-1], factory = [[-2,1],[2,1]]
 * Output: 2
 * Explanation: As shown in the figure:
 * - The first robot at position 1 moves in the positive direction. It will be repaired at the second factory.
 * - The second robot at position -1 moves in the negative direction. It will be repaired at the first factory.
 * The limit of the first factory is 1, and it fixed 1 robot.
 * The limit of the second factory is 1, and it fixed 1 robot.
 * The total distance is |2 - 1| + |(-2) - (-1)| = 2. It can be shown that we cannot achieve a better total distance than 2.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= robot.length, factory.length <= 100
 * factory[j].length == 2
 * -109 <= robot[i], positionj <= 109
 * 0 <= limitj <= robot.length
 * The input will be generated such that it is always possible to repair every robot.
 */
public class MinimumTotalDistanceTraveled {
    public long minimumTotalDistance(List<Integer> robots, int[][] factory) {
        List<Integer> factories = new ArrayList<>();
        for (int i = 0; i < factory.length; i++) {
            for (int j = 0; j < factory[i][1]; j++) {
                factories.add(factory[i][0]);
            }
        }

        Collections.sort(robots);
        Collections.sort(factories);

        long[][] dp = new long[robots.size() + 1][factories.size() + 1];
        for (int i = 0; i < dp.length; i++) {
            Arrays.fill(dp[i], -1);
        }

        return count(robots, factories, 0, 0, dp);
    }

    private long count(List<Integer> robots, List<Integer> factories, int robotIndex, int factoryIndex, long[][] dp) {
        if (robotIndex == robots.size()) return 0;
        if (factoryIndex == factories.size()) return Long.MAX_VALUE;

        if (dp[robotIndex][factoryIndex] != -1) {
            return dp[robotIndex][factoryIndex];
        }
        long distance = Math.abs(robots.get(robotIndex) - factories.get(factoryIndex));
        long factoryIncluded = count(robots, factories, robotIndex + 1, factoryIndex + 1, dp);
        if (factoryIncluded != Long.MAX_VALUE) {
            factoryIncluded += distance;
        }
        long factoryExcluded = count(robots, factories, robotIndex, factoryIndex + 1, dp);

        dp[robotIndex][factoryIndex] = Math.min(factoryIncluded, factoryExcluded);
        return dp[robotIndex][factoryIndex];
    }
}
