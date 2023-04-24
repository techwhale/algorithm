package org.practise.algorithm.leetcode.string.hard;

import java.util.*;

public class RemoveInvalidParantheses {
    public List<String> removeInvalidParentheses(String input) {
        Set<String> validParantheses = new HashSet<>();
        backtrack(0, input, validParantheses, new StringBuilder());

        Map<Integer, List<String>> result = new HashMap<>();
        int max = 0;
        for (String val : validParantheses){
            int count = countParentheses(val);
            List<String> tempList = result.getOrDefault(count, new ArrayList<>());
            if (! tempList.isEmpty()) {
                String prevVal = tempList.get(0);
                if (prevVal.length() < val.length()){
                    tempList.clear();
                } else if (val.length() == prevVal.length()) {
                    tempList.add(val);
                }
            } else {
                tempList.add(val);
            }

            result.put(count, tempList);
            if (max < count) {
                max = count;
            }
        }
        return result.get(max);
    }

    private void backtrack(int startIdx, String input, Set<String> result, StringBuilder current){
        if (startIdx == input.length()){
            String currentString = current.toString();
            if (isValidParentheses(currentString)) {
                result.add(currentString);
            }
            return;
        }

        for (int i = startIdx; i < input.length(); i++) {
            StringBuilder next = new StringBuilder(current);
            next.append(input.charAt(i));
            backtrack(i + 1, input, result, next);
        }
    }

    private boolean isValidParentheses(String parantheses) {
        int count = 0;
        for (int i = 0; i < parantheses.length(); i++) {
            if (parantheses.charAt(i) == '(') {
                count++;
            } else if (parantheses.charAt(i) == ')'){
                count--;
            }
            if (count < 0)
                return false;
        }
        return count == 0;
    }

    private int countParentheses(String input) {
        int parentheses = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(' || input.charAt(i) == ')') {
                parentheses++;
            }
        }
        return parentheses;
    }
}
