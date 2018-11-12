package org.practise.algorithm.interviewbit.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Given a set of candidate numbers (C) and a target number (T), find all unique combinations in C where the candidate numbers sums to T.
 *
 * The same repeated number may be chosen from C unlimited number of times.
 *
 *  Note:
 * All numbers (including target) will be positive integers.
 * Elements in a combination (a1, a2, … , ak) must be in non-descending order. (ie, a1 ≤ a2 ≤ … ≤ ak).
 * The combinations themselves must be sorted in ascending order.
 * CombinationA > CombinationB iff (a1 > b1) OR (a1 = b1 AND a2 > b2) OR … (a1 = b1 AND a2 = b2 AND … ai = bi AND ai+1 > bi+1)
 * The solution set must not contain duplicate combinations.
 * Example,
 * Given candidate set 2,3,6,7 and target 7,
 * A solution set is:
 *
 * [2, 2, 3]
 * [7]
 *  Warning : DO NOT USE LIBRARY FUNCTION FOR GENERATING COMBINATIONS.
 * Example : itertools.combinations in python.
 * If you do, we will disqualify your submission retroactively and give you penalty points.
 */
public class CombinationSum {
    public List<List<Integer>> combinationSum(List<Integer> list, int target) {
        List<List<Integer>> resultList = new ArrayList<>();
        Collections.sort(list);
        combinationSumDFS(0, list, new ArrayList<>(), resultList, target);
        return resultList;
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> resultList = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        for (int i : candidates) list.add(i);
        Collections.sort(list);
        combinationSumDFS(0, list, new ArrayList<>(), resultList, target);
        return resultList;
    }

    private void combinationSumDFS(int index, List<Integer> list, List<Integer> tempList, List<List<Integer>> resultList, int target) {
        if (target == 0) {
            resultList.add(new ArrayList<>(tempList));
            return;
        }
        for (int i = index; i < list.size(); i++) {
            int value = list.get(i);
            int newTarget = target - value;
            if (newTarget < 0)
                break;
            if (i > 0 && list.get(i) == list.get(i - 1)) continue;
            tempList.add(value);
            combinationSumDFS(i, list, tempList, resultList, newTarget);
            tempList.remove(tempList.size() - 1);
        }
    }
}
