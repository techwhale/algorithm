package org.practise.algorithm.leetcode.tree.hard;

import org.practise.algorithm.pojo.TreeNode;

/**
 * 988. Smallest String Starting From Leaf
 * Medium
 *
 * You are given the root of a binary tree where each node has a value in the range [0, 25] representing the letters 'a' to 'z'.
 * Return the lexicographically smallest string that starts at a leaf of this tree and ends at the root.
 *
 * As a reminder, any shorter prefix of a string is lexicographically smaller.
 *
 * For example, "ab" is lexicographically smaller than "aba".
 * A leaf of a node is a node that has no children.
 *
 * Example 1:
 * Input: root = [0,1,2,3,4,3,4]
 * Output: "dba"
 *
 * Example 2:
 * Input: root = [25,1,3,1,3,0,2]
 * Output: "adz"
 *
 * Example 3:
 * Input: root = [2,2,1,null,1,0,null,0]
 * Output: "abc"
 *
 * Constraints:
 * The number of nodes in the tree is in the range [1, 8500].
 * 0 <= Node.val <= 25
 */
public class SmallestStringFromLeaf {
    private String result;
    public String smallestFromLeaf(TreeNode root) {
        dfs(root, new StringBuilder());
        return result;
    }

    private void dfs(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append((char) (node.val + 'a'));
        if (node.left == null && node.right == null) {
            sb.reverse();
            String current = sb.toString();
            sb.reverse();
            if (result == null || current.compareTo(result) < 0) {
                result = current;
            }
        }
        dfs(node.left, sb);
        dfs(node.right, sb);
        sb.deleteCharAt(sb.length() - 1);

    }
}
