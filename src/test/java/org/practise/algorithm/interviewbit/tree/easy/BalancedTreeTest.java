package org.practise.algorithm.interviewbit.tree.easy;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BalancedTreeTest {
    private BalancedTree balancedTreeObj = new BalancedTree();

    @Test
    public void testBalancedTree() {
        final TreeNode treeNode = formTreeNode();
        Assert.assertEquals(balancedTreeObj.isBalanced(treeNode), 0);
    }

    private TreeNode formTreeNode() {
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        node4.right = node5;
        node3.left = node4;
        node1.left = node2; node1.right = node3;
        return node1;
    };
}