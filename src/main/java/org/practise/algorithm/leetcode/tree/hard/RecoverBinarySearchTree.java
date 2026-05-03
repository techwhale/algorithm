package org.practise.algorithm.leetcode.tree.hard;

import org.practise.algorithm.pojo.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

public class RecoverBinarySearchTree {
    public void recoverTree(TreeNode root) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode x = null, y = null, prev = null;
        while(!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.add(root);
                root = root.left;
            }
            TreeNode node = stack.removeLast();
            if (prev != null && node.val < prev.val) {
                y = node;
                if (x == null) { x = prev;}
                else {
                    break;
                }
            }
            prev = node;
            root = node.right;
        }
        swap(x, y);
    }

    private void swap(TreeNode node1, TreeNode node2) {
        int temp = node1.val;
        node1.val = node2.val;
        node2.val = temp;
    }
}
