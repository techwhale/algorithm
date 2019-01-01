package org.practise.algorithm.leetcode.string.easy;

import java.util.Arrays;

/**
 *
 Given two non-negative integers num1 and num2 represented as strings, return the product of num1 and num2, also represented as a string.

 Example 1:

 Input: num1 = "2", num2 = "3"
 Output: "6"
 Example 2:

 Input: num1 = "123", num2 = "456"
 Output: "56088"
 Note:

 The length of both num1 and num2 is < 110.
 Both num1 and num2 contain only digits 0-9.
 Both num1 and num2 do not contain any leading zero, except the number 0 itself.
 You must not use any built-in BigInteger library or convert the inputs to integer directly.
 */
public class MultiplyStrings {
    public String multiply(String num1, String num2) {
        byte[] digits = new byte[num1.length() + num2.length()];
        Arrays.fill(digits, (byte) 0);
        num1 = "0" + num1;

        int m = num2.length(), n = num1.length();
        for (int i = m - 1; i >= 0; i--) {
            int carry = 0, startPosition = digits.length - 1 - (m - 1 - i);
            for (int j = n - 1; j >= 0; j--) {
                int index = startPosition - (n - 1 - j);
                int sum = carry + (num2.charAt(i) - '0') * (num1.charAt(j) - '0') + digits[index];
                carry = sum / 10;
                sum = sum % 10;
                digits[index] = (byte) sum;
            }
        }

        StringBuilder builder = new StringBuilder();
        boolean nonZeroFound = false;
        for (byte num : digits) {
            if (! nonZeroFound && num > 0) {
                nonZeroFound = true;
            }
            if (nonZeroFound)
                builder.append((char) ('0'+ num));
        }

        return builder.length() > 0 ? builder.toString() : "0";
    }
}
