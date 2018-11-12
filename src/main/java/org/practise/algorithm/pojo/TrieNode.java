package org.practise.algorithm.pojo;

public class TrieNode {
    private TrieNode[] links;
    private final int R = 26;
    private boolean isEnd = false;

    public TrieNode() {
        links = new TrieNode[R];
    }

    public void setEnd() {
        this.isEnd = true;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public boolean containsKey(char ch) {
        return  links[ch - 'a'] != null;
    }

    public void put(char ch, TrieNode node) {
        links[ch - 'a'] = node;
    }

    public TrieNode get(char ch) {
        return links[ch - 'a'];
    }
}
