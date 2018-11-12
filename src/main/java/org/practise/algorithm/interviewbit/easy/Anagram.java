package org.practise.algorithm.interviewbit.easy;

import java.util.*;

public class Anagram {
    // DO NOT MODIFY THE LIST. IT IS READ ONLY
    public ArrayList<ArrayList<Integer>> anagrams(final List<String> list) {
        ArrayList<ArrayList<Integer>> resultList = new ArrayList<>();
        if (list == null || list.isEmpty())
            return resultList;
        Map<String, ArrayList<Integer>> anagramIndexMap = new HashMap<>();
        for (int i = 1; i <= list.size(); i++) {
            char[] arr = list.get(i - 1).toCharArray();
            Arrays.sort(arr);
            final String key = new String(arr);
            ArrayList<Integer>indexList = anagramIndexMap.getOrDefault(key, new ArrayList<>());
            indexList.add(i);
            anagramIndexMap.putIfAbsent(key, indexList);
        }

        for (String str : anagramIndexMap.keySet()) {
            ArrayList<Integer> tempList = anagramIndexMap.get(str);
            resultList.add(tempList);
        }
        return resultList;
    }
}
