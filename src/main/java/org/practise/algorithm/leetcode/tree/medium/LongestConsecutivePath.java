package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;

public class LongestConsecutivePath {
    public int findLongestPath(TreeNode root) {
        if ( root == null) return 0;
        return findLongestPath(root, null, 0);
    }

    private int findLongestPath(TreeNode node, Integer previous, int current) {
        if (node == null)
            return 0;
        if (previous == null || node.val  != ((long) previous) + 1) {
            current = 1;
        } else {
            current = current + 1;
        }
        int max = Math.max(current, Math.max(findLongestPath(node.left, node.val, current),
                findLongestPath(node.right, node.val, current)));
        return max;
    }

}
