package org.practise.algorithm.leetcode.string.medium;

import java.util.Arrays;
import java.util.Stack;

/**
 * 678. Valid Parenthesis String
 *
 * Given a string s containing only three types of characters: '(', ')' and '*', return true if s is valid.
 *
 * The following rules define a valid string:
 *
 * Any left parenthesis '(' must have a corresponding right parenthesis ')'.
 * Any right parenthesis ')' must have a corresponding left parenthesis '('.
 * Left parenthesis '(' must go before the corresponding right parenthesis ')'.
 * '*' could be treated as a single right parenthesis ')' or a single left parenthesis '(' or an empty string "".
 *
 *
 * Example 1:
 *
 * Input: s = "()"
 * Output: true
 * Example 2:
 *
 * Input: s = "(*)"
 * Output: true
 * Example 3:
 *
 * Input: s = "(*))"
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s[i] is '(', ')' or '*'
 */
public class ValidParanthesis {

    public boolean checkValidString2(String s) {
        int n = s.length();
        int[][] memo = new int[n][n];
        for (int[] row: memo) {
            Arrays.fill(row, -1);
        }
        return isValidString(0, 0, s, memo);
    }

    private boolean isValidString(int index, int openCount, String str, int[][] memo) {
        // If reached end of the string, check if all brackets are balanced
        if (index == str.length()) {
            return (openCount == 0);
        }
        // If already computed, return memoized result
        if (memo[index][openCount] != -1) {
            return memo[index][openCount] == 1;
        }
        boolean isValid = false;
        // If encountering '*', try all possibilities
        if (str.charAt(index) == '*') {
            isValid |= isValidString(index + 1, openCount + 1, str, memo); // Treat '*' as '('
            if (openCount > 0) {
                isValid |= isValidString(index + 1, openCount - 1, str, memo); // Treat '*' as ')'
            }
            isValid |= isValidString(index + 1, openCount, str, memo); // Treat '*' as empty
        } else {
            // Handle '(' and ')'
            if (str.charAt(index) == '(') {
                isValid = isValidString(index + 1, openCount + 1, str, memo); // Increment count for '('
            } else if (openCount > 0) {
                isValid = isValidString(index + 1, openCount - 1, str, memo); // Decrement count for ')'
            }
        }

        // Memoize and return the result
        memo[index][openCount] = isValid ? 1 : 0;
        return isValid;
    }

    public boolean checkValidString(String s) {
        Stack<Integer> openBrackets = new Stack<>();
        Stack<Integer> astericks = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                openBrackets.push(i);
            } else if (ch == ')') {
                if (! openBrackets.isEmpty()) {
                    openBrackets.pop();
                } else if (! astericks.isEmpty()) {
                    astericks.pop();
                } else {
                    return false;
                }
            } else if (ch == '*') {
                astericks.push(i);
            }
        }
        while (! openBrackets.isEmpty() && ! astericks.isEmpty()) {
            if (openBrackets.pop() > astericks.pop()) {
                return false;
            }
        }
        return openBrackets.isEmpty();
    }


    public boolean checkValidString3(String s) {
        int open = 0, close = 0;
        int n = s.length() - 1;
        for (int i = 0; i <= n; i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '*') {
                open++;
            } else {
                open--;
            }
            if (s.charAt(n - i) == ')' || s.charAt(n - i) == '*') {
                close++;
            } else {
                close--;
            }

            if (open < 0 || close < 0) {
                return false;
            }
        }
        return true;
    }
}
