package org.practise.algorithm.interviewbit.hashing;

import java.util.HashMap;
import java.util.Map;

/**
 * Given two integers representing the numerator and denominator of a fraction, return the fraction in string format.
 *
 * If the fractional part is repeating, enclose the repeating part in parentheses.
 *
 * Example :
 *
 * Given numerator = 1, denominator = 2, return "0.5"
 * Given numerator = 2, denominator = 1, return "2"
 * Given numerator = 2, denominator = 3, return "0.(6)"
 */
public class Fraction {
    public String fractionToDecimal(int dividend, int divisor) {
        if (dividend == 0) return "0";
        StringBuilder fraction = new StringBuilder();
        if (divisor < 0 ^ dividend < 0) {
            fraction.append("-");
        }
        long numerator = Math.abs(dividend * 1l), denominator =  Math.abs(divisor * 1l);
        fraction.append(numerator / denominator);
        long remainder = numerator % denominator;
        if (remainder != 0)
            fraction.append(".");

        Map<Long, Integer> remainderPosition = new HashMap<>();
        while (remainder != 0) {
            if (remainderPosition.containsKey(remainder)) {
                int position = remainderPosition.get(remainder);
                fraction.insert(position, '(');
                fraction.append(")");
                break;
            }
            remainderPosition.put(remainder, fraction.length());
            remainder = remainder * 10;
            fraction.append(remainder / denominator);
            remainder = remainder % denominator;
        }
        return fraction.toString();
    }
}
