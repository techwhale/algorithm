package org.practise.algorithm.interviewbit.tree.easy;

import org.practise.algorithm.pojo.TreeNode;

import java.util.Stack;

/**
 * Given a binary search tree, write a function to find the kth smallest element in the tree.
 *
 * Example :
 *
 * Input :
 *   2
 *  / \
 * 1   3
 *
 * and k = 2
 *
 * Return : 2
 *
 * As 2 is the second smallest element in the tree.
 *  NOTE : You may assume 1 <= k <= Total number of nodes in BST
 */
public class KthSmallestElementInTree {
    public int kthsmallest(TreeNode root, int k) {
        int count = 0;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        while (current != null || ! stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            TreeNode node = stack.pop();
            if (++count == k) return node.val;
            current = node.right;
        }
        return Integer.MIN_VALUE;
    }
}
