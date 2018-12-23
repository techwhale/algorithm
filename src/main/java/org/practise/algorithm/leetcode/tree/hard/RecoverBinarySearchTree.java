package org.practise.algorithm.leetcode.tree.hard;

import org.practise.algorithm.pojo.TreeNode;

public class RecoverBinarySearchTree {
    public void recoverTree(TreeNode root) {
        TreeNode minNode = new TreeNode(Integer.MIN_VALUE);
        TreeNode maxNode = new TreeNode(Integer.MAX_VALUE);
        fixBinarySearchTree(root, minNode, maxNode);
    }

    private void fixBinarySearchTree(TreeNode root, TreeNode min, TreeNode max) {
        if (root == null) return;
        if (min.val < root.val && root.val < max.val) {
            fixBinarySearchTree(root.left, min, root);
            fixBinarySearchTree(root.right, root, max);
        } else if (min.val > root.val) {
            swap(root, min);
        } else {
            swap(root, max);
        }
    }

    private void swap(TreeNode node1, TreeNode node2) {
        int temp = node1.val;
        node1.val = node2.val;
        node2.val = temp;
    }
}
