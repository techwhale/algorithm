package org.practise.algorithm.leetcode.string.easy;

public class ValidPalindrome {
    public boolean validPalindrome(String s) {
        return validPalindrome(s, 0 , s.length() - 1, true);
    }

    public boolean validPalindrome(String word, int startindex, int endindex, boolean canRemoveChar) {
        boolean valid;
        if (endindex < startindex) return true;
        if (word.charAt(startindex) == word.charAt(endindex)) {
            valid = validPalindrome(word, startindex + 1, endindex - 1, canRemoveChar);
        } else {
            if (canRemoveChar) {
                valid = validPalindrome(word, startindex + 1, endindex, false) || validPalindrome(word, startindex, endindex - 1, false);
            } else { // already used canRemoveChar
                valid = false;
            }
        }
        return valid;
    }

    public boolean isPalindrome(String s) {
        for (int left_index = 0, right_index = s.length() -1; left_index < right_index; ) {
            char leftCharacter = s.charAt(left_index);
            char rightCharacter = s.charAt(right_index);

            if (! Character.isAlphabetic(leftCharacter)) {
                left_index++;
                continue;
            }

            if (! Character.isAlphabetic(rightCharacter)) {
                right_index--;
                continue;
            }

            if (Character.toLowerCase(leftCharacter) != Character.toLowerCase(rightCharacter))  return false;
        }
        return true;
    }
}
