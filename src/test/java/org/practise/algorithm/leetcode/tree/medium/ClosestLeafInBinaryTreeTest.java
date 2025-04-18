package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ClosestLeafInBinaryTreeTest {

    private ClosestLeafInBinaryTree closestLeafInBinaryTree = new ClosestLeafInBinaryTree();
    @Test
    public void testFindClosestLeaf() {
        TreeNode root = new TreeNode(1);
        TreeNode left = new TreeNode(3);
        TreeNode right = new TreeNode(2);
        root.left = left;
        root.right = right;
        closestLeafInBinaryTree.findClosestLeaf(root, 1);
    }
}