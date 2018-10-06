package org.practise.algorithm.interviewbit.string.easy;

/**
 Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.

 Example:

 "A man, a plan, a canal: Panama" is a palindrome.

 "race a car" is not a palindrome.

 Return 0 / 1 ( 0 for false, 1 for true ) for this problem
 */
public class PalindromeString {
    public int isPalindrome(String A) {
        if (A == null || A.isEmpty()) {
            return 0;
        }
        int startIndex = 0, endIndex = A.length() - 1;
        while (startIndex < endIndex) {
            char startChar = Character.toLowerCase(A.charAt(startIndex));
            char endChar = Character.toLowerCase(A.charAt(endIndex));

            if (! validCharacter(startChar)) {
                startIndex++;
                continue;
            }

            if (! validCharacter(endChar)) {
                endIndex--;
                continue;
            }

            if (startChar != endChar) {
                return 0;
            }
            startIndex++;
            endIndex--;
        }
        return 1;
    }

    private boolean validCharacter(char ch) {
        return ('a' <= ch && ch <= 'z') || ('0' <= ch && ch <= '9');
    }
}
