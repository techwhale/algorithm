package org.practise.algorithm.interviewbit.stackandqueue;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Evaluate the value of an arithmetic expression in Reverse Polish Notation.
 *
 * Valid operators are +, -, *, /. Each operand may be an integer or another expression.
 *
 * Examples:
 *
 *   ["2", "1", "+", "3", "*"] -> ((2 + 1) * 3) -> 9
 *   ["4", "13", "5", "/", "+"] -> (4 + (13 / 5)) -> 6
 */
public class EvaluateExpression {
    public int evalRPN(ArrayList<String> A) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < A.size(); i++) {
            String str = A.get(i);
            if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/")) {
                int second = stack.pop();
                int first = stack.pop();
                int result = compute(first, second, str);
                stack.push(result);
            } else {
                stack.push(Integer.parseInt(str));
            }
        }
        return stack.pop();
    }

    private int compute(int first, int second, String operator) {
        int result = 0;
        if (operator.equals("+")) {
            result = first + second;
        } else if (operator.equals("-")) {
            result = first - second;
        } else if (operator.equals("*")) {
            result = first * second;
        } else {
            result = first / second;
        }
        return result;
    }
}
