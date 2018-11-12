package org.practise.algorithm.interviewbit.tree.easy;

import org.practise.algorithm.pojo.TreeNode;

/**
 * Given a binary tree, find its maximum depth.
 *
 * The maximum depth of a binary tree is the number of nodes along the longest path from the root node down to the farthest leaf node.
 *
 *  NOTE : The path has to end on a leaf node.
 * Example :
 *
 *          1
 *         /
 *        2
 * max depth = 2.
 */
public class MaxDepthOfBinaryTree {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
}
