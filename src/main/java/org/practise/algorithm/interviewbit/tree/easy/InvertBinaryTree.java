package org.practise.algorithm.interviewbit.tree.easy;

import org.practise.algorithm.pojo.TreeNode;

/**
 * Given a binary tree, invert the binary tree and return it.
 * Look at the example for more details.
 *
 * Example :
 * Given binary tree
 *
 *      1
 *    /   \
 *   2     3
 *  / \   / \
 * 4   5 6   7
 * invert and return
 *
 *      1
 *    /   \
 *   3     2
 *  / \   / \
 * 7   6 5   4
 */
public class InvertBinaryTree {
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode tempLeft = root.left;
        root.left = root.right;
        root.right = tempLeft;
        invertTree(root.right);
        invertTree(root.left);
        return root;
    }
}
