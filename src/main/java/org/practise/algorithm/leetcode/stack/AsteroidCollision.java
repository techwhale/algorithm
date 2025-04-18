package org.practise.algorithm.leetcode.stack;

import java.util.LinkedList;
import java.util.Stack;

/**
 * 735. Asteroid Collision
 * Medium
 * Topics
 * Companies
 * Hint
 * We are given an array asteroids of integers representing asteroids in a row.
 *
 * For each asteroid, the absolute value represents its size, and the sign represents its direction (positive meaning right, negative meaning left). Each asteroid moves at the same speed.
 *
 * Find out the state of the asteroids after all collisions. If two asteroids meet, the smaller one will explode. If both are the same size, both will explode. Two asteroids moving in the same direction will never meet.
 *
 * Example 1:
 *
 * Input: asteroids = [5,10,-5]
 * Output: [5,10]
 * Explanation: The 10 and -5 collide resulting in 10. The 5 and 10 never collide.
 *
 * Example 2:
 * Input: asteroids = [8,-8]
 * Output: []
 * Explanation: The 8 and -8 collide exploding each other.
 * Example 3:
 *
 * Input: asteroids = [10,2,-5]
 * Output: [10]
 * Explanation: The 2 and -5 collide resulting in -5. The 10 and -5 collide resulting in 10.
 *
 *
 * Constraints:
 *
 * 2 <= asteroids.length <= 104
 * -1000 <= asteroids[i] <= 1000
 * asteroids[i] != 0
 */
public class AsteroidCollision {
    public int[] asteroidCollision(int[] asteroids) {
        Stack<Integer> stack = new Stack<>();

        for (int i = asteroids.length - 1; i >= 0; i--) {
            int currentAsteroid = asteroids[i];
            if (
                    stack.isEmpty()
                            // traveling in same direction
                            || (currentAsteroid > 0 && stack.peek() > 0) || (currentAsteroid < 0 && stack.peek() < 0)
                            // traveling in opposite direction and never meet scenario. For e.g. [-5, 10]
                            || (currentAsteroid < 0 && stack.peek() > 0)
            ) {
                stack.push(currentAsteroid);
            } else {
                int lastAsteroid = stack.pop();
                if (Math.abs(lastAsteroid) > Math.abs(currentAsteroid)) {
                    stack.push(lastAsteroid);
                } else if (Math.abs(lastAsteroid) < Math.abs(currentAsteroid)) {
                    i++;
                }
            }
        }

        int[] result = new int[stack.size()];
        for (int i = 0; i < result.length ; i++) {
            result[i] = stack.pop();
        }
        return result;
    }
}
