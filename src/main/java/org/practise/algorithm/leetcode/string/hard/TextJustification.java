package org.practise.algorithm.leetcode.string.hard;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an array of words and a width maxWidth, format the text such that each line has exactly maxWidth characters and is fully (left and right) justified.
 *
 * You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces ' ' when necessary so that each line has exactly maxWidth characters.
 *
 * Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line do not divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.
 *
 * For the last line of text, it should be left justified and no extra space is inserted between words.
 *
 * Note:
 *
 * A word is defined as a character sequence consisting of non-space characters only.
 * Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
 * The input array words contains at least one word.
 * Example 1:
 *
 * Input:
 * words = ["This", "is", "an", "example", "of", "text", "justification."]
 * maxWidth = 16
 * Output:
 * [
 *    "This    is    an",
 *    "example  of text",
 *    "justification.  "
 * ]
 * Example 2:
 *
 * Input:
 * words = ["What","must","be","acknowledgment","shall","be"]
 * maxWidth = 16
 * Output:
 * [
 *   "What   must   be",
 *   "acknowledgment  ",
 *   "shall be        "
 * ]
 * Explanation: Note that the last line is "shall be    " instead of "shall     be",
 *              because the last line must be left-justified instead of fully-justified.
 *              Note that the second line is also left-justified becase it contains only one word.
 * Example 3:
 *
 * Input:
 * words = ["Science","is","what","we","understand","well","enough","to","explain",
 *          "to","a","computer.","Art","is","everything","else","we","do"]
 * maxWidth = 20
 * Output:
 * [
 *   "Science  is  what we",
 *   "understand      well",
 *   "enough to explain to",
 *   "a  computer.  Art is",
 *   "everything  else  we",
 *   "do                  "
 * ]
 */
public class TextJustification {
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> tempWordsList = new ArrayList<>();
        List<String> justifiedResultList = new ArrayList<>();

        int characterCount = 0;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int totalchar = characterCount + word.length();

            // check whether total character exceed max width
            if (totalchar > maxWidth) {
                justifiedResultList.add(formJustifiedText(tempWordsList, maxWidth, false));
                tempWordsList.clear();
                characterCount = 0;
                i--; // not processed current word, move index to previous position
            } else {
                tempWordsList.add(word);
                characterCount += word.length() + 1; // include atleast one space
            }
        }

        if (! tempWordsList.isEmpty()) {
            justifiedResultList.add(formJustifiedText(tempWordsList, maxWidth, true));
        }
        return justifiedResultList;
    }

    private String formJustifiedText(List<String> tempWordsList, int maxWidth, boolean isLastLine) {
        StringBuilder builder = new StringBuilder();
        if (! isLastLine) {
            int gapsCount = tempWordsList.size() - 1;

            int charCount = 0;
            for (int i = 0 ; i < tempWordsList.size(); i++) {
                charCount += tempWordsList.get(i).length();
            }
            int allowedGaps =  maxWidth - charCount;
            int carry = gapsCount == 0 ? allowedGaps : allowedGaps % gapsCount;
            int gapPerWord =  gapsCount == 0 ? 0 : allowedGaps / gapsCount;

            for (int i = 0; i < tempWordsList.size(); i++) {
                builder.append(tempWordsList.get(i));

                if (tempWordsList.size() - 1  == 0 || i < tempWordsList.size() - 1) {
                    int c = tempWordsList.size() - 1  == 0 ? carry : Math.min(1, carry);
                    for (int gaps = gapPerWord + c; gaps > 0; gaps--) {
                        builder.append(" ");
                    }
                }
                if (carry > 0) carry--;
            }

        } else {
            for (String word : tempWordsList) {
                builder.append(word);
                builder.append(" ");
            }
            builder.setLength(builder.length() - 1);
            for (int space_remaining = maxWidth - builder.length(); space_remaining > 0; space_remaining--) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
}
