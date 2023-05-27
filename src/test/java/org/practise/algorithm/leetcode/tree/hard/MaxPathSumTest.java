package org.practise.algorithm.leetcode.tree.hard;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MaxPathSumTest {
    private MaxPathSum obj = new MaxPathSum();
    @Test
    public void testMaxPathSum() {
        TreeNode treeNode15 = new TreeNode(15);
        TreeNode treeNode7 = new TreeNode(7);
        TreeNode treeNode20 = new TreeNode(20);
        treeNode20.left = treeNode15;
        treeNode20.right = treeNode7;
        TreeNode treeNode9 = new TreeNode(9);
        TreeNode treeNodeMinus10 = new TreeNode(-10);
        treeNodeMinus10.left = treeNode9;
        treeNodeMinus10.right = treeNode20;
        Assert.assertEquals(obj.maxPathSum(treeNodeMinus10), 42);
    }
}