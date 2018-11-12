package org.practise.algorithm.leetcode.contest;

import org.practise.algorithm.pojo.TreeNode;

public class RangeBST {
    public int rangeSumBST(TreeNode root, int L, int R) {
        if (root == null)return 0;
        return rangeSumBST(root.left, L, R) + rangeSumBST(root.right, L, R) + (L <= root.val && root.val <= R ? root.val : 0);
    }
}
