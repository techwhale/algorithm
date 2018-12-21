package org.practise.algorithm.basics;

public class ReverseWord {
    public String reverseWords(String s) {
        if (s == null || s.isEmpty())
            return s;

        String words[] = s.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            builder.append(words[i]);
            builder.append(" ");
        }
        if (builder.length() > 0) builder.setLength(builder.length() - 1); // remove last extra space added
        return builder.toString();
    }
}
