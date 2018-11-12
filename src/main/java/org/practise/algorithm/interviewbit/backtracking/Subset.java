package org.practise.algorithm.interviewbit.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a set of distinct integers, S, return all possible subsets.
 *
 *  Note:
 * Elements in a subset must be in non-descending order.
 * The solution set must not contain duplicate subsets.
 * Also, the subsets should be sorted in ascending ( lexicographic ) order.
 * The list is not necessarily sorted.
 * Example :
 *
 * If S = [1,2,3], a solution is:
 *
 * [
 *   [],
 *   [1],
 *   [1, 2],
 *   [1, 2, 3],
 *   [1, 3],
 *   [2],
 *   [2, 3],
 *   [3],
 * ]
 */
public class Subset {
    public ArrayList<ArrayList<Integer>> subsets(ArrayList<Integer> list) {
        ArrayList<ArrayList<Integer>> resultList = new ArrayList<>();
        subset(list, new ArrayList<>(), resultList);
        return resultList;
    }

    private void subset(List<Integer> list, ArrayList<Integer> tempResultList, ArrayList<ArrayList<Integer>> resultList) {
        resultList.add(tempResultList);
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            List<Integer> temp = new ArrayList<>(list);
            int value = temp.get(i);
            temp = temp.subList(i + 1, temp.size());

            ArrayList<Integer> tempResult = new ArrayList<>(tempResultList);
            tempResult.add(value);
            subset(temp, tempResult, resultList);
        }
    }
}
