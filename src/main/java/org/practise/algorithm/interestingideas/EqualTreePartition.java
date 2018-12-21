package org.practise.algorithm.interestingideas;

import org.practise.algorithm.pojo.TreeNode;

import java.util.Stack;

/**
 * Given a binary tree with n nodes, your task is to check if it's possible to partition the tree to two trees which have
 * the equal sum of values after removing exactly one edge on the original tree.
 *
 * Example 1:
 * Input:
 *     5
 *    / \
 *   10 10
 *     /  \
 *    2   3
 *
 * Output: True
 * Explanation:
 *     5
 *    /
 *   10
 *
 * Sum: 15
 *
 *    10
 *   /  \
 *  2    3
 *
 * Sum: 15
 * Example 2:
 * Input:
 *     1
 *    / \
 *   2  10
 *     /  \
 *    2   20
 *
 * Output: False
 * Explanation: You can't split the tree into two trees with equal sum after removing exactly one edge on the tree.
 * Note:
 * The range of tree node value is in the range of [-100000, 100000].
 * 1 <= n <= 10000
 */
public class EqualTreePartition {
//    public boolean checkEqualTree(TreeNode root) {
//        if (root == null)
//            return false;
//        Stack<Integer> stack = new Stack<>();
//        sum(root, stack);
//        int total = stack.pop();
//
//        if (total % 2 == 0) {
//            while(! stack.isEmpty()) {
//                if (total / 2 == stack.pop()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    private int sum(TreeNode node, Stack<Integer> stack) {
//        if (node == null) return 0;
//        stack.push(node.val + sum(node.left, stack) + sum(node.right, stack));
//        return stack.peek();
//    }


    public boolean checkEqualTree(TreeNode root) {
        if (root == null) return false;
        final int total = sum(root);
        return isEqualTree(root.left, total) || isEqualTree(root.right, total);
    }

    private int sum(TreeNode root) {
        if (root == null) return 0;
        root.val = root.val + sum(root.left) + sum(root.right);
        return root.val;
    }

    private boolean isEqualTree(TreeNode node, int sum) {
        if (node == null) return false;
        if (node.val * 2 == sum) {
            return true;
        }
        return isEqualTree(node.left, sum) || isEqualTree(node.right, sum);
    }
}
