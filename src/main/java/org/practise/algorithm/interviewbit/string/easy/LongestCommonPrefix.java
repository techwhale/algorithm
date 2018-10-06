package org.practise.algorithm.interviewbit.string.easy;

import java.util.ArrayList;
import java.util.List;

public class LongestCommonPrefix {
    public String longestCommonPrefix(List<String> A) {
        if (A == null || A.isEmpty()) {
            return "";
        }
        if (A.size() == 1) {
            return A.get(0);
        }

        String result = A.get(0);
        for (int i = 1; i < A.size(); i++) {
            if (result.isEmpty()) {
                return result;
            }

            String str = A.get(i);
            int minimumLength = Math.min(str.length(), result.length());
            result = result.substring(0, minimumLength);
            str = str.substring(0, minimumLength);
            int strIndex = 0;
            while (strIndex < str.length()) {
                if (result.charAt(strIndex) != str.charAt(strIndex)) {
                    result = result.substring(0, strIndex);
                    break;
                }
                strIndex++;
            }
        }
        return result;
    }
}
