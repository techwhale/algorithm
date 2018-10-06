package org.practise.algorithm.interviewbit.string.easy;

/**
 The count-and-say sequence is the sequence of integers beginning as follows:

 1, 11, 21, 1211, 111221, ...
 1 is read off as one 1 or 11.
 11 is read off as two 1s or 21.

 21 is read off as one 2, then one 1 or 1211.

 Given an integer n, generate the nth sequence.

 Note: The sequence of integers will be represented as a string.

 Example:

 if n = 2,
 the sequence is 11.
 */
public class CountAndSay {
    public String countAndSay(int A) {
        StringBuilder stringPattern = new StringBuilder("1");
        while (A > 1) {
            StringBuilder builder = new StringBuilder();
            char lastSeenChar = stringPattern.charAt(0);
            int count = 1;
            for (int i = 1; i <= stringPattern.length(); i++) {
                if (i == stringPattern.length()) {
                    builder.append(count);
                    builder.append(lastSeenChar);
                    break;
                }
                char currentChar = stringPattern.charAt(i);
                if (currentChar == lastSeenChar) {
                    count++;
                } else {
                    builder.append(count);
                    builder.append(lastSeenChar);
                    lastSeenChar = currentChar;
                    count = 1;
                }
            }

            stringPattern = builder;
            A--;
        }
        return stringPattern.toString();
    }
}
