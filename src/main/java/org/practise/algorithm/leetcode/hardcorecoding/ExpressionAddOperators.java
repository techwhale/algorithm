package org.practise.algorithm.leetcode.hardcorecoding;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string that contains only digits 0-9 and a target value, return all possibilities to add binary operators (not unary)
 * +, -, or * between the digits so they evaluate to the target value.
 *
 * Example 1:
 *
 * Input: num = "123", target = 6
 * Output: ["1+2+3", "1*2*3"]
 * Example 2:
 *
 * Input: num = "232", target = 8
 * Output: ["2*3+2", "2+3*2"]
 * Example 3:
 *
 * Input: num = "105", target = 5
 * Output: ["1*0+5","10-5"]
 * Example 4:
 *
 * Input: num = "00", target = 0
 * Output: ["0+0", "0-0", "0*0"]
 * Example 5:
 *
 * Input: num = "3456237490", target = 9191
 * Output: []
 */
public class ExpressionAddOperators {
    public List<String> answer;
    public String digits;
    public long target;


    public List<String> addOperators(String num, int target) {
        this.answer = new ArrayList<>();
        this.digits = num;
        this.target = target;
        recurse(0, 0, new StringBuilder(), 0);
        return answer;
    }

    private void recurse(int index, long value, StringBuilder operation, long previousValue) {
        String num = this.digits;

        if (index == num.length()) {
            if (value == target) {
                answer.add(operation.toString());
            }
            return;
        }

        long current_value = 0;
        String current_value_str = null;

        for (int i = index; i < num.length(); i++) {
            current_value = current_value * 10 + Character.getNumericValue(num.charAt(i));
            current_value_str = Long.toString(current_value);

            if (index == 0) {
                recurse(i + 1, current_value, new StringBuilder(operation).append(current_value_str), current_value);
            } else {
                // MULTIPLICATION
                long v = value - previousValue;
                recurse(i + 1, v + previousValue * current_value, new StringBuilder(operation).append("*").append(current_value_str), previousValue * current_value);

                // ADDITION
                recurse(i + 1, value + current_value, new StringBuilder(operation).append("+").append(current_value_str), current_value);

                // SUBTRACTION
                recurse(i + 1, value - current_value, new StringBuilder(operation).append("-").append(current_value_str), - current_value);

            }
            // Avoid operands like 025. If the current index is 0, it should be an operand in itself.
            if (num.charAt(index) == '0') {
                break;
            }
        }
    }
}
