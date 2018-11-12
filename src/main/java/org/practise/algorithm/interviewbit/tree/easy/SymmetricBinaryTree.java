package org.practise.algorithm.interviewbit.tree.easy;


import org.practise.algorithm.pojo.TreeNode;

/**
 * Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
 *
 * Example :
 *
 *     1
 *    / \
 *   2   2
 *  / \ / \
 * 3  4 4  3
 * The above binary tree is symmetric.
 * But the following is not:
 *
 *     1
 *    / \
 *   2   2
 *    \   \
 *    3    3
 * Return 0 / 1 ( 0 for false, 1 for true ) for this problem
 */
public class SymmetricBinaryTree {
    public int isSymmetric(TreeNode A) {
        return isSymmetric(A.left, A.right) ? 1 : 0;
    }

    private boolean isSymmetric(TreeNode A, TreeNode B) {
        if (A == null && B == null) return true;
        if (A == null || B == null) return false;
        return A.val == B.val && isSymmetric(A.left, B.right) && isSymmetric(A.right, B.left);
    }
}
