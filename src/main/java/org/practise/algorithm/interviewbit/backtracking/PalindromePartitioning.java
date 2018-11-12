package org.practise.algorithm.interviewbit.backtracking;

import java.util.ArrayList;

/**
 * Given a string s, partition s such that every string of the partition is a palindrome.
 *
 * Return all possible palindrome partitioning of s.
 *
 * For example, given s = "aab",
 * Return
 *
 *   [
 *     ["a","a","b"]
 *     ["aa","b"],
 *   ]
 *  Ordering the results in the answer : Entry i will come before Entry j if :
 * len(Entryi[0]) < len(Entryj[0]) OR
 * (len(Entryi[0]) == len(Entryj[0]) AND len(Entryi[1]) < len(Entryj[1])) OR
 * *
 * *
 * *
 * (len(Entryi[0]) == len(Entryj[0]) AND … len(Entryi[k] < len(Entryj[k]))
 * In the given example,
 * ["a", "a", "b"] comes before ["aa", "b"] because len("a") < len("aa")
 */
public class PalindromePartitioning {
    public ArrayList<ArrayList<String>> partition(String str) {
        ArrayList<ArrayList<String>> resultList = new ArrayList<>();
        ArrayList<String> tempResultList = new ArrayList<>();
        if (str != null && ! str.isEmpty())
            findPartition(str, tempResultList, resultList);
        return resultList;
    }

    private void findPartition(String str, ArrayList<String> tempResultList, ArrayList<ArrayList<String>> resultList) {
        if (str.length() == 0) {
            if (! tempResultList.isEmpty()) {
                resultList.add(new ArrayList<>(tempResultList));
            }
            return;
        }
        for (int i = 0; i < str.length(); i++) {
            if (isPalindrome(str, 0, i)) {
                tempResultList.add(str.substring(0, i + 1));
                findPartition(str.substring(i + 1), tempResultList, resultList);
                tempResultList.remove(tempResultList.size() - 1);
            }
        }
    }

    private boolean isPalindrome(String str, int i, int j) {
        while (i < j) {
            if (str.charAt(i) != str.charAt(j))
                return false;
            i++;
            j--;
        }
        return true;
    }
}
