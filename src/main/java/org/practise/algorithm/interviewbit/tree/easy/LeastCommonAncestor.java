package org.practise.algorithm.interviewbit.tree.easy;

import org.practise.algorithm.pojo.TreeNode;

/**
 * Find the lowest common ancestor in an unordered binary tree given two values in the tree.
 *
 *  Lowest common ancestor : the lowest common ancestor (LCA) of two nodes v and w in a tree or
 *  directed acyclic graph (DAG) is the lowest (i.e. deepest) node that has both v and w as descendants.
 * Example :
 *
 *
 *         _______3______
 *        /              \
 *     ___5__          ___1__
 *    /      \        /      \
 *    6      _2_     0        8
 *          /   \
 *          7    4
 * For the above tree, the LCA of nodes 5 and 1 is 3.
 *
 *  LCA = Lowest common ancestor
 * Please note that LCA for nodes 5 and 4 is 5.
 *
 * You are given 2 values. Find the lowest common ancestor of the two nodes represented by val1 and val2
 * No guarantee that val1 and val2 exist in the tree. If one value doesn’t exist in the tree then return -1.
 * There are no duplicate values.
 * You can use extra memory, helper functions, and can modify the node struct but, you can’t add a parent pointer.
 */
public class LeastCommonAncestor {
    public int lca(TreeNode root, int val1, int val2) {
        if (! findNode(root, val1) || ! findNode(root, val2)) return -1;
        TreeNode result = LCA(root, val1 ,val2);
        return result.val;
    }

    public boolean findNode(TreeNode root, int val) {
        if (root == null) return false;
        return root.val == val || findNode(root.left, val) || findNode(root.right, val);
    }

    public TreeNode LCA(TreeNode root, int val1, int val2) {
        if (root == null || root.val == val1 || root.val == val2) {
            return root;
        }
        TreeNode left = LCA(root.left, val1, val2);
        TreeNode right = LCA(root.right, val1, val2);
        if (left != null && right != null) {
            return root;
        }
        return left != null ? left : right;
    }
}
