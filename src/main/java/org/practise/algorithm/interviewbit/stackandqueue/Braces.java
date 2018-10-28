package org.practise.algorithm.interviewbit.stackandqueue;

import java.util.Stack;

/**
 * Write a program to validate if the input string has redundant braces?
 * Return 0/1
 *
 * 0 -> NO
 * 1 -> YES
 * Input will be always a valid expression
 *
 * and operators allowed are only + , * , - , /
 *
 * Example:
 *
 * ((a + b)) has redundant braces so answer will be 1
 * (a + (a + b)) doesn't have have any redundant braces so answer will be 0
 */
public class Braces {
    public int braces(String A) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < A.length(); i++) {
            char ch = A.charAt(i);
            if (isOperator(ch) || ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                char lastPusedChar = stack.peek();
                if (! isOperator(lastPusedChar)) return 1;
                else {
                    stack.pop();
                    stack.pop();
                }
            }
        }
        return 0;
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }
}
