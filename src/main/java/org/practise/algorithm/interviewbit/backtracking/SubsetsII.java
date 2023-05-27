package org.practise.algorithm.interviewbit.backtracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Given a collection of integers that might contain duplicates, S, return all possible subsets.
 *
 *  Note:
 * Elements in a subset must be in non-descending order.
 * The solution set must not contain duplicate subsets.
 * The subsets must be sorted lexicographically.
 * Example :
 * If S = [1,2,2], the solution is:
 *
 * [
 * [],
 * [1],
 * [1,2],
 * [1,2,2],
 * [2],
 * [2, 2]
 * ]
 */
public class SubsetsII {
//    public ArrayList<ArrayList<Integer>> subsetsWithDup(ArrayList<Integer> list) {
//        ArrayList<ArrayList<Integer>> resultList = new ArrayList<>();
//        Collections.sort(list);
//        subset(list, new ArrayList<>(), resultList);
//        return resultList;
//    }
//
//    private void subset(List<Integer> list, ArrayList<Integer> tempResultList, ArrayList<ArrayList<Integer>> resultList) {
//        resultList.add(tempResultList);
//        if (list == null || list.isEmpty()) {
//            return;
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (i > 0 && list.get(i) == list.get(i - 1))
//                continue;
//            List<Integer> temp = new ArrayList<>(list);
//            int value = temp.get(i);
//            temp = temp.subList(i + 1, temp.size());
//
//            ArrayList<Integer> tempResult = new ArrayList<>(tempResultList);
//            tempResult.add(value);
//            subset(temp, tempResult, resultList);
//        }
//    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (int num : nums) {
            List<List<Integer>> tempList = new ArrayList<>(result);
            for (List<Integer> list : tempList){
                list.add(num);
                result.add(list);
            }
        }
        return result;
    }

//    public List<List<Integer>> subsets(int[] nums) {
//        List<List<Integer>> output = new ArrayList();
//        output.add(new ArrayList<Integer>());
//
//        for (int num : nums) {
//            List<List<Integer>> newSubsets = new ArrayList();
//            for (List<Integer> curr : output) {
//                newSubsets.add(new ArrayList<Integer>(curr){{add(num);}});
//            }
//            for (List<Integer> curr : newSubsets) {
//                output.add(curr);
//            }
//        }
//        return output;
//    }
}
