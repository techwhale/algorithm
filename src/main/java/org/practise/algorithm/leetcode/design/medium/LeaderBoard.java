package org.practise.algorithm.leetcode.design.medium;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *1244. Design A Leaderboard
 * Medium
 * 678
 * 88
 * company
 * Bloomberg
 * company
 * Amazon
 * company
 * Google
 * Design a Leaderboard class, which has 3 functions:
 *
 * addScore(playerId, score): Update the leaderboard by adding score to the given player's score. If there is no player with such id in the leaderboard, add him to the leaderboard with the given score.
 * top(K): Return the score sum of the top K players.
 * reset(playerId): Reset the score of the player with the given id to 0 (in other words erase it from the leaderboard). It is guaranteed that the player was added to the leaderboard before calling this function.
 * Initially, the leaderboard is empty.
 *
 * Example 1:
 *
 * Input:
 * ["Leaderboard","addScore","addScore","addScore","addScore","addScore","top","reset","reset","addScore","top"]
 * [[],[1,73],[2,56],[3,39],[4,51],[5,4],[1],[1],[2],[2,51],[3]]
 * Output:
 * [null,null,null,null,null,null,73,null,null,null,141]
 *
 * Explanation:
 * Leaderboard leaderboard = new Leaderboard ();
 * leaderboard.addScore(1,73);   // leaderboard = [[1,73]];
 * leaderboard.addScore(2,56);   // leaderboard = [[1,73],[2,56]];
 * leaderboard.addScore(3,39);   // leaderboard = [[1,73],[2,56],[3,39]];
 * leaderboard.addScore(4,51);   // leaderboard = [[1,73],[2,56],[3,39],[4,51]];
 * leaderboard.addScore(5,4);    // leaderboard = [[1,73],[2,56],[3,39],[4,51],[5,4]];
 * leaderboard.top(1);           // returns 73;
 * leaderboard.reset(1);         // leaderboard = [[2,56],[3,39],[4,51],[5,4]];
 * leaderboard.reset(2);         // leaderboard = [[3,39],[4,51],[5,4]];
 * leaderboard.addScore(2,51);   // leaderboard = [[2,51],[3,39],[4,51],[5,4]];
 * leaderboard.top(3);           // returns 141 = 51 + 51 + 39;
 *
 *
 * Constraints:
 *
 * 1 <= playerId, K <= 10000
 * It's guaranteed that K is less than or equal to the current number of players.
 * 1 <= score <= 100
 * There will be at most 1000 function calls.
 */
public class LeaderBoard {
    private Map<Integer, Integer> mapPlayerScore;
    private TreeMap<Integer, Integer> mapTopScore;
    public LeaderBoard() {
        mapPlayerScore = new HashMap<>();
        mapTopScore = new TreeMap<>(Comparator.reverseOrder());
    }

    public void addScore(int playerId, int score) {
        if (mapPlayerScore.containsKey(playerId)) {
            int prevScore = mapPlayerScore.get(playerId);
            int newScore = prevScore + score;;
            mapPlayerScore.put(playerId, newScore);
            updateTopScore(prevScore, - 1);
            updateTopScore(newScore, 1);
        } else {
            mapPlayerScore.put(playerId, score);
            updateTopScore(score, 1);
        }
    }

    public int top(int K) {
        int sum = 0;
        for (int score : mapTopScore.keySet()) {
            if (K == 0) break;
            int count = mapTopScore.get(score);
            while (count > 0) {
                if (K == 0) break;
                sum += score;
                K--;
                count--;
            }
        }
        return sum;
    }

    public void reset(int playerId) {
        int oldScore = mapPlayerScore.get(playerId);
        mapPlayerScore.put(playerId, 0);
        updateTopScore(oldScore, -1);
        updateTopScore(0, 1);
    }

    private void updateTopScore(int score, int valueToAdd){
        int count = mapTopScore.getOrDefault(score, 0) + valueToAdd;
        if (count == 0) {
            mapTopScore.remove(score);
        } else {
            mapTopScore.put(score, count);
        }
    }
}
