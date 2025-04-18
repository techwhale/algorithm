package org.practise.algorithm.leetcode.stack;

import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class AsteroidCollisionTest {
    private AsteroidCollision asteroidCollision = new AsteroidCollision();
    @Test
    public void testAsteroidCollision() {
        int[] asteroids1 = new int[] {5, 10, -5};
        int[] asteroids2 = new int[] {8, -8};
        int[] asteroids3 = new int[] {10, 2, -5};

        int[] results1 = new int[] {5, 10};
        int[] results2 = new int[] {};
        int[] results3 = new int[] {10};

        assertTrue(Arrays.equals(asteroidCollision.asteroidCollision(asteroids1), results1));
        assertTrue(Arrays.equals(asteroidCollision.asteroidCollision(asteroids2), results2));
        assertTrue(Arrays.equals(asteroidCollision.asteroidCollision(asteroids3), results3));
    }
}