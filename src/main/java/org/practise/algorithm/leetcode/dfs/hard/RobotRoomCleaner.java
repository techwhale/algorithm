package org.practise.algorithm.leetcode.dfs.hard;

/**
 * Given a robot cleaner in a room modeled as a grid.
 *
 * Each cell in the grid can be empty or blocked.
 *
 * The robot cleaner with 4 given APIs can move forward, turn left or turn right. Each turn it made is 90 degrees.
 *
 * When it tries to move into a blocked cell, its bumper sensor detects the obstacle and it stays on the current cell.
 *
 * Design an algorithm to clean the entire room using only the 4 given APIs shown below.
 *
 * interface Robot {
 *   // returns true if next cell is open and robot moves into the cell.
 *   // returns false if next cell is obstacle and robot stays on the current cell.
 *   boolean move();
 *
 *   // Robot will stay on the same cell after calling turnLeft/turnRight.
 *   // Each turn will be 90 degrees.
 *   void turnLeft();
 *   void turnRight();
 *
 *   // Clean the current cell.
 *   void clean();
 * }
 * Example:
 *
 * Input:
 * room = [
 *   [1,1,1,1,1,0,1,1],
 *   [1,1,1,1,1,0,1,1],
 *   [1,0,1,1,1,1,1,1],
 *   [0,0,0,1,0,0,0,0],
 *   [1,1,1,1,1,1,1,1]
 * ],
 * row = 1,
 * col = 3
 *
 * Explanation:
 * All grids in the room are marked by either 0 or 1.
 * 0 means the cell is blocked, while 1 means the cell is accessible.
 * The robot initially starts at the position of row=1, col=3.
 * From the top left corner, its position is one row below and three columns right.
 * Notes:
 *
 * The input is only given to initialize the room and the robot's position internally. You must solve this problem "blindfolded".
 * In other words, you must control the robot using only the mentioned 4 APIs, without knowing the room layout and the initial robot's position.
 * The robot's initial position will always be in an accessible cell.
 * The initial direction of the robot will be facing up.
 * All accessible cells are connected, which means the all cells marked as 1 will be accessible by the robot.
 * Assume all four edges of the grid are all surrounded by wall
 */

import java.util.HashSet;
import java.util.Set;

/**
 * // This is the robot's control interface.
 * // You should not implement it, or speculate about its implementation
 * interface Robot {
 *     // Returns true if the cell in front is open and robot moves into the cell.
 *     // Returns false if the cell in front is blocked and robot stays in the current cell.
 *     public boolean move();
 *
 *     // Robot will stay in the same cell after calling turnLeft/turnRight.
 *     // Each turn will be 90 degrees.
 *     public void turnLeft();
 *     public void turnRight();
 *
 *     // Clean the current cell.
 *     public void clean();
 * }
 */
public class RobotRoomCleaner {

    interface Robot {
        public boolean move();
        public void turnLeft();
        public void turnRight();
        public void clean();
    }

    private static int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    public void cleanRoom(Robot robot) {
        Set<Cell> visited = new HashSet<>();
        visited.add(new Cell(0, 0));
        cleanRoom(robot, 0, 0, 0, visited);
    }

    private void cleanRoom(Robot robot, int facingDirection, int row, int column, Set<Cell> visited) {
        robot.clean();
        for (int i = 0; i < 4; i++) {
            int direction = (facingDirection + i) % 4;
            int nextRow = row + DIRECTIONS[direction][0], nextColumn = column + DIRECTIONS[direction][1];
            Cell nextCell = new Cell(nextRow, nextColumn);
            if (! visited.contains(nextCell) && robot.move()) {
                visited.add(nextCell);
                cleanRoom(robot, direction, nextRow, nextColumn, visited);
            }
            robot.turnRight();
        }
        robot.turnRight();
        robot.turnRight();
        robot.move();
        robot.turnRight();
        robot.turnRight();
    }

    class Cell {
        int row;
        int column;
        public Cell(int r, int c) {
            this.row = r;
            this.column = c;
        }

        public int hashCode() {
            return this.row * 31 + column;
        }

        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || o.getClass() != this.getClass())
                return false;
            Cell that = (Cell) o;
            return this.row == that.row && this.column == that.column;
        }
    }
}
