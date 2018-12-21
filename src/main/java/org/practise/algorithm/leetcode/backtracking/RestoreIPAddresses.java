package org.practise.algorithm.leetcode.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string containing only digits, restore it by returning all possible valid IP address combinations.
 *
 * Example:
 *
 * Input: "25525511135"
 * Output: ["255.255.11.135", "255.255.111.35"]
 */
public class RestoreIPAddresses {
    private static final int MAX_IP_PARTITION = 4;
    public List<String> restoreIpAddresses(String s) {
        List<String> resultList = new ArrayList<>(MAX_IP_PARTITION);
        final ArrayList<Integer> tempIPList = new ArrayList<>(MAX_IP_PARTITION);
        for (int i = 0; i < MAX_IP_PARTITION; i++) {
            tempIPList.add(0);
        }
        restoreIpAddresses(0, 0, s, tempIPList, resultList);
        return resultList;
    }

    private void restoreIpAddresses(int position, int index, String s, List<Integer> tempIPList, List<String> resultList) {
        if (position == MAX_IP_PARTITION) {
            if (index == s.length()) {
                resultList.add(convertToIp(tempIPList));
            } else {
                return;
            }
        } else {
            StringBuilder builder = new StringBuilder();
            for (int idx = index; idx < s.length(); idx++) {
                builder.append(s.charAt(idx));
                if (! isValidIpSection(builder.toString())) break;
                tempIPList.set(position, Integer.parseInt(builder.toString()));
                restoreIpAddresses(position + 1, idx + 1, s, tempIPList, resultList);
            }
        }
    }

    private boolean isValidIpSection(String section) {
        if (section.charAt(0) == '0' && section.length() > 1)
            return false;
        int val = Integer.parseInt(section);
        return 0 <= val && val <= 255;
    }

    private String convertToIp(List<Integer> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i) + ".");
        }
        builder.setLength(builder.length() - 1); // remove last dot character;
        return builder.toString();
    }
}
