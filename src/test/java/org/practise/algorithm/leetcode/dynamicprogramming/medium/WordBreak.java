package org.practise.algorithm.leetcode.dynamicprogramming.medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Given a string s and a dictionary of words dict,
 * determine if s can be segmented into a space-separated sequence of one or more dictionary words.
 *
 * For example, given
 *
 * s = "myinterviewtrainer",
 * dict = ["trainer", "my", "interview"].
 * Return 1 ( corresponding to true ) because "myinterviewtrainer" can be segmented as "my interview trainer".
 *
 * Return 0 / 1 ( 0 for false, 1 for true ) for this problem
 */
public class WordBreak {
//    public int wordBreak(String word, List<String> dictionaryWords) {
//        Set<String> dict = new HashSet<>(dictionaryWords);
//        int N = word.length();
//        boolean[][] wordexist = new boolean[N][N];
//        for (int i = 0; i < N; i++)
//            wordexist[i][i] = dict.contains(word.charAt(i)+ "");
//
//        for (int len = 2; len <= N; len++) {
//            for (int i = 0; i + len - 1 < N; i++) {
//                if (dict.contains(word.substring(i, i + len))) {
//                    wordexist[i][i + len - 1] = true;
//                } else {
//                    for (int k = 1; k < len; k++) {
//                        if (wordexist[i][i + k - 1] && wordexist[i + k][i + len - 1]) {
//                            wordexist[i][i + len - 1] = true;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        return wordexist[0][N - 1] ? 1 : 0;
//    }

    // O(S) complexity
    public int wordBreak(String word, List<String> dictionaryWords) {
        Set<String> dict = new HashSet<>(dictionaryWords);
        int N = word.length();
        boolean[] dp = new boolean[N];
        List<Integer> matched_index = new ArrayList<>();
        matched_index.add(-1);
        for (int i = 0; i < N; i++) {
            int size = matched_index.size();
            boolean foundword = false;
            for (int j = size - 1; j >= 0; j--) {
                String str = word.substring(matched_index.get(j) + 1, i + 1);
                if (dict.contains(str)) {
                    foundword = true;
                    break;
                }
            }

            if (foundword) {
                dp[i] = true;
                matched_index.add(i);
            }
        }
        return dp[N - 1] ? 1 : 0;
    }
}
