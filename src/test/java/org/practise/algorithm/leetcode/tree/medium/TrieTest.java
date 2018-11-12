package org.practise.algorithm.leetcode.tree.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TrieTest {
    private Trie trie = new Trie();

    /**
     *  * trie.insert("apple");
     *  * trie.search("apple");   // returns true
     *  * trie.search("app");     // returns false
     *  * trie.startsWith("app"); // returns true
     *  * trie.insert("app");
     *  * trie.search("app");     // returns true
     *  * Note:
     *  *
     */
    @Test
    public void testTrie() {
        trie.insert("apple");
        Assert.assertTrue(trie.search("apple"));
        Assert.assertFalse(trie.search("app"));
        Assert.assertTrue(trie.startsWith("app"));
    }
}