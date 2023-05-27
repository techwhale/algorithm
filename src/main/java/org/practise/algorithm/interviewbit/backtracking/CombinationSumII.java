package org.practise.algorithm.interviewbit.backtracking;

import java.util.*;

/**
 * Given a collection of candidate numbers (C) and a target number (T), find all unique combinations in C where the candidate numbers sums to T.
 *
 * Each number in C may only be used once in the combination.
 *
 *  Note:
 * All numbers (including target) will be positive integers.
 * Elements in a combination (a1, a2, … , ak) must be in non-descending order. (ie, a1 ≤ a2 ≤ … ≤ ak).
 * The solution set must not contain duplicate combinations.
 * Example :
 *
 * Given candidate set 10,1,2,7,6,1,5 and target 8,
 *
 * A solution set is:
 *
 * [1, 7]
 * [1, 2, 5]
 * [2, 6]
 * [1, 1, 6]
 *  Warning : DO NOT USE LIBRARY FUNCTION FOR GENERATING COMBINATIONS.
 * Example : itertools.combinations in python.
 * If you do, we will disqualify your submission retroactively and give you penalty points.
 */
public class CombinationSumII {
    public ArrayList<ArrayList<Integer>> combinationSum(List<Integer> list, int target) {
        ArrayList<ArrayList<Integer>> resultList = new ArrayList<>();
        Collections.sort(list);
        combinationSumDFS(0, list, new ArrayList<>(), resultList, target);
        return resultList;
    }

    private void combinationSumDFS(int index, List<Integer> list, List<Integer> tempList, ArrayList<ArrayList<Integer>> resultList, int target) {
        if (target == 0) {
            resultList.add(new ArrayList<>(tempList));
        }
        if (index >= list.size()) {
            return;
        }
        for (int i = index; i < list.size(); i++) {
            int value = list.get(i);

            int newTarget = target - value;
            if (newTarget < 0)
                break;
            tempList.add(value);
            combinationSumDFS(i + 1, list, tempList, resultList, newTarget);
            tempList.remove(tempList.size() - 1);
            while (i < list.size() - 1 && list.get(i) == list.get(i + 1))
                i++;
        }
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(0, target, candidates, new LinkedList<>(), result);
        return result;
    }

    private void backtrack(int index, int target, int[] candidates, LinkedList<Integer> current, List<List<Integer>> result){
        if (target == 0) {
            result.add(new ArrayList<>(current));
        }
        if (target < 0 || index == candidates.length) {
            return;
        }

        for (int i = index; i < candidates.length; i++) {
            if (i > index && candidates[i] == candidates[i - 1]) {
                continue;
            }

            current.add(candidates[i]);
            backtrack(i + 1, target - candidates[i], candidates, current, result);
            current.removeLast();
        }
    }
}
