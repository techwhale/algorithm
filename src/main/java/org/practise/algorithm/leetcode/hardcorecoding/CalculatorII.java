package org.practise.algorithm.leetcode.hardcorecoding;

/**
 * Implement a basic calculator to evaluate a simple expression string.
 *
 * The expression string contains only non-negative integers, +, -, *, / operators and empty spaces.
 * The integer division should truncate toward zero.
 *
 * Example 1:
 *
 * Input: "3+2*2"
 * Output: 7
 * Example 2:
 *
 * Input: " 3/2 "
 * Output: 1
 * Example 3:
 *
 * Input: " 3+5 / 2 "
 * Output: 5
 * Note:
 *
 * You may assume that the given expression is always valid.
 * Do not use the eval built-in library function.
 */
public class CalculatorII {
    public int calculate(String s) {
        if (s == null || s.isEmpty())
            return 0;
        s = s.trim().replaceAll(" +", "");
        int result = 0, previousValue = 0, currentValue = 0, index = 0, N = s.length();
        char sign = '+';

        while (index < N) {
            currentValue = 0;
            char ch = s.charAt(index);
            while (index < N && ch - '0' >= 0 && ch - '9' <= 9) {
                currentValue = currentValue * 10 +  (ch - '0');
                index++;
                ch = index == N ? 'A' : s.charAt(index);
            }

            if (sign == '+') {
                result += previousValue;
                previousValue = currentValue;
            } else if (sign == '-') {
                result += previousValue;
                previousValue = - currentValue;
            } else if (sign == '*') {
                previousValue = previousValue * currentValue;
            } else if (sign == '/') {
                previousValue = previousValue / currentValue;
            }

            if (index < N)
                sign = s.charAt(index);
            index++;
        }
        return result + previousValue;
    }
}
