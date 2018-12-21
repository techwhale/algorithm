package org.practise.algorithm.leetcode.hardcorecoding;

import java.util.Stack;

/**
 * Given an encoded string, return it's decoded string.
 *
 * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times.
 * Note that k is guaranteed to be a positive integer.
 *
 * You may assume that the input string is always valid; No extra white spaces, square brackets are well-formed, etc.
 *
 * Furthermore, you may assume that the original data does not contain any digits and that digits are only for those repeat numbers, k.
 * For example, there won't be input like 3a or 2[4].
 *
 * Examples:
 *
 * s = "3[a]2[bc]", return "aaabcbc".
 * s = "3[a2[c]]", return "accaccacc".
 * s = "2[abc]3[cd]ef", return "abcabccdcdcdef".
 */
public class DecodeString {
    public String decodeString(String s) {
        Stack<Integer> countStack = new Stack<>();
        Stack<StringBuilder> stringStack = new Stack<>();
        int index = 0, count = 0;
        StringBuilder result = new StringBuilder();
        while (index < s.length()) {
            char ch = s.charAt(index);
            if (Character.isDigit(ch)) {
                while (Character.isDigit(ch)) {
                    count = count * 10 + (ch - '0');
                    index++;
                    ch = s.charAt(index);
                }
                countStack.push(count);
                count = 0;
            } else if (ch == '[') {
                stringStack.push(result);
                result = new StringBuilder();
                index++;
            } else if (ch == ']') {
                int times = countStack.pop();
                // append to previous string number of times
                StringBuilder prev = stringStack.pop();
                for (; times > 0; times--) {
                    prev.append(result);
                }
                result = prev;
                index++;
            } else {
                result.append(ch);
                index++;
            }
        }
        return result.toString();
    }
}
