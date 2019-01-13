package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LongestConsecutivePathTest {
    private LongestConsecutivePath obj = new LongestConsecutivePath();

    @Test
    public void testLongestConsecutivePath() {
        TreeNode node1 = new TreeNode(1);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        TreeNode node4 = new TreeNode(4);
        node1.left = node3;
        node1.right = node2;
        node2.right = node4;
        Assert.assertEquals(obj.findLongestPath(node1), 2);
    }

    @Test
    public void testLongestConsecutivePath2() {
        TreeNode node1 = new TreeNode(1);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        TreeNode node4 = new TreeNode(4);
        node1.left = node4;
        node1.right = node2;
        node2.right = node3;
        Assert.assertEquals(obj.findLongestPath(node1), 3);
    }


    @Test
    public void testLongestConsecutivePath3() {
        TreeNode node1 = new TreeNode(1);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        TreeNode node4 = new TreeNode(4);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        TreeNode node8 = new TreeNode(8);

        node1.left = node4;
        node1.right = node2;
        node2.right = node3;

        node3.left = node6;
        node3.right = node7;

        node2.left = node8;

        Assert.assertEquals(obj.findLongestPath(node1), 3);
    }
}