package org.practise.algorithm.leetcode.string;

import java.util.ArrayList;
import java.util.List;

/**
 We are given two arrays A and B of words.  Each word is a string of lowercase letters.

 Now, say that word b is a subset of word a if every letter in b occurs in a, including multiplicity.  For example, "wrr" is a subset of "warrior", but is not a subset of "world".

 Now say a word a from A is universal if for every b in B, b is a subset of a.

 Return a list of all universal words in A.  You can return the words in any order.



 Example 1:

 Input: A = ["amazon","apple","facebook","google","leetcode"], B = ["e","o"]
 Output: ["facebook","google","leetcode"]
 Example 2:

 Input: A = ["amazon","apple","facebook","google","leetcode"], B = ["l","e"]
 Output: ["apple","google","leetcode"]
 Example 3:

 Input: A = ["amazon","apple","facebook","google","leetcode"], B = ["e","oo"]
 Output: ["facebook","google"]
 Example 4:

 Input: A = ["amazon","apple","facebook","google","leetcode"], B = ["lo","eo"]
 Output: ["google","leetcode"]
 Example 5:

 Input: A = ["amazon","apple","facebook","google","leetcode"], B = ["ec","oc","ceo"]
 Output: ["facebook","leetcode"]


 Note:

 1 <= A.length, B.length <= 10000
 1 <= A[i].length, B[i].length <= 10
 A[i] and B[i] consist only of lowercase letters.
 All words in A[i] are unique: there isn't i != j with A[i] == A[j].
 */
public class WordSubsets {
    private static final int DEFAULT_CHAR = 26;
    public List<String> wordSubsets(String[] A, String[] B) {

        int[] bCharCount = countChar("");
        for (String b : B) {
            int[] currentCharCount = countChar(b);
            for (int i = 0; i < DEFAULT_CHAR; i++)
                bCharCount[i] = Math.max(currentCharCount[i], bCharCount[i]);
        }

        List<String> passedList = new ArrayList<>();
        search: for (String a : A) {
            int[] aCharCount = countChar(a);
            for (int i = 0; i < DEFAULT_CHAR; i++) {
                if (aCharCount[i] < bCharCount[i]) continue search;
            }
            passedList.add(a);
        }
        return passedList;
    }

    private int[] countChar(String word) {
        int[] count = new int[DEFAULT_CHAR];
        if (word != null || ! word.isEmpty()) {
            char[] chars = word.toCharArray();
            for (char ch: chars)
                count[ch  - 'a']++;
        }
        return count;
    }
}
