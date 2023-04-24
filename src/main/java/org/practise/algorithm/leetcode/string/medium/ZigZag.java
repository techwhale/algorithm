package org.practise.algorithm.leetcode.string.medium;

/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this: (you may want to display this pattern in a fixed font for better legibility)
 *
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * And then read line by line: "PAHNAPLSIIGYIR"
 * Write the code that will take a string and make this conversion given a number of rows:
 * string convert(string s, int numRows);
 *
 * Example 1:
 * Input: s = "PAYPALISHIRING", numRows = 3
 * Output: "PAHNAPLSIIGYIR"
 * Example 2:
 *
 * Input: s = "PAYPALISHIRING", numRows = 4
 * Output: "PINALSIGYAHRPI"
 * Explanation:
 * P     I    N
 * A   L S  I G
 * Y A   H R
 * P     I
 * Example 3:
 *
 * Input: s = "A", numRows = 1
 * Output: "A"
 *
 * Constraints:
 * 1 <= s.length <= 1000
 * s consists of English letters (lower-case and upper-case), ',' and '.'.
 * 1 <= numRows <= 1000
 */
public class ZigZag {
    public String convert(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }
        int gap = 2 * (numRows - 1);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < numRows; i++) {
            int currIndex = i;

            while(currIndex < s.length()) {
                result.append(s.charAt(currIndex));

                if (i != 0  && i != numRows - 1) {
                    int secondGap = gap - 2 * i;
                    int secondIndex = currIndex + secondGap;
                    if (secondIndex < s.length()) {
                        result.append(s.charAt(secondIndex));
                    }
                }
                currIndex += gap;
            }
        }
        return result.toString();
    }
}
