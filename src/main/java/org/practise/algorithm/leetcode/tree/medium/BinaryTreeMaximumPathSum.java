package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;

public class BinaryTreeMaximumPathSum {
    public int maxPathSum(TreeNode root) {
        MaxPathSum maxPathSum = new MaxPathSum(Integer.MIN_VALUE);
        maxPathSum(root, maxPathSum);
        return maxPathSum.value;
    }

    private int maxPathSum(TreeNode node, MaxPathSum maxPathSum) {
        if (node == null) {
            return Integer.MIN_VALUE;
        }
        int leftNodeValue = Math.max(0, maxPathSum(node.left, maxPathSum)),
                rightNodeValue = Math.max(0, maxPathSum(node.right, maxPathSum));
        int sum = node.val + leftNodeValue + rightNodeValue;
        maxPathSum.setSumIfGreater(sum);
        return  node.val + Math.max(leftNodeValue, rightNodeValue);
    }

    class MaxPathSum {
        int value;
        public MaxPathSum(int sum) {
            this.value = sum;
        }

        public void setSumIfGreater(int sum) {
            this.value = Math.max(sum, value);
        }
    }
}
