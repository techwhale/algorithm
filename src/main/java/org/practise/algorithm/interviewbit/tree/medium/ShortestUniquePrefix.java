package org.practise.algorithm.interviewbit.tree.medium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Find shortest unique prefix to represent each word in the list.
 *
 * Example:
 *
 * Input: [zebra, dog, duck, dove]
 * Output: {z, dog, du, dov}
 * where we can see that
 * zebra = z
 * dog = dog
 * duck = du
 * dove = dov
 * NOTE : Assume that no word is prefix of another. In other words, the representation is always possible.
 */
public class ShortestUniquePrefix {
    class TrieNode {
        Map<Character, TrieNode> map;
        Map<Character, Integer> charCount;
        public TrieNode(){
            map = new HashMap<>();
            charCount = new HashMap<>();
        }

        public void insert(String word) {
            TrieNode current = this;
            for (char ch : word.toCharArray()) {
                Map<Character, TrieNode> map = current.map;
                Map<Character, Integer> charCount = current.charCount;
                if ( ! map.containsKey(ch)) {
                    map.put(ch, new TrieNode());
                    charCount.put(ch, 0);
                }
                charCount.put(ch, charCount.getOrDefault(ch, 0) + 1);
                current = map.get(ch);
            }
        }

        public String shortPrefix(String word) {
            StringBuilder prefix = new StringBuilder();
            TrieNode current = this;
            for (char ch : word.toCharArray()) {
                prefix.append(ch);
                if (current.charCount.get(ch) == 1) break;
                current = current.map.get(ch);
            }
            return prefix.toString();
        }
    }
    public ArrayList<String> prefix(ArrayList<String> words) {
        TrieNode node = new TrieNode();
        for (String word : words) {
            node.insert(word);
        }

        ArrayList<String> resultList = new ArrayList<>();
        for (String word : words) {
            resultList.add(node.shortPrefix(word));
        }
        return resultList;
    }
}
