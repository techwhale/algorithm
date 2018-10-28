package org.practise.algorithm.leetcode.string;

import java.util.HashSet;
import java.util.Set;

public class UniqueEmails {
    public int numUniqueEmails(String[] emails) {
        Set<String> uniqueEmails = new HashSet<>();
        for (String email : emails) {
            boolean plusEncountered = false, atEncountered = false;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < email.length(); i++) {
                final char ch = email.charAt(i);
                if (ch == '@') atEncountered = true;
                if (! atEncountered && ch == '.') continue;
                if (ch == '+') {
                    plusEncountered = true;
                    continue;
                }
                if (plusEncountered & ! atEncountered) continue;
                builder.append(ch);
            }
            uniqueEmails.add(builder.toString());
        }
        return uniqueEmails.size();
    }
}
