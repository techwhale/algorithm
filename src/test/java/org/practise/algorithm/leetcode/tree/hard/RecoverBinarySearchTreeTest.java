package org.practise.algorithm.leetcode.tree.hard;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RecoverBinarySearchTreeTest {
    private RecoverBinarySearchTree obj = new RecoverBinarySearchTree();
    @Test
    public void testRecoverBinarySearchTree() {
        final TreeNode treeNode = formTreeNode();
        obj.recoverTree(treeNode);
    }

    private TreeNode formTreeNode() {
        TreeNode node1 = new TreeNode(1);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        node1.left = node3;
        node3.right = node2;
        return node1;
    };
}