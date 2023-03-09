package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BinaryTreeInfectionTest {

    @Test
    public void testAmountOfTime() {
        TreeNode node1 = new TreeNode(1);
        TreeNode node5 = new TreeNode(5);

        TreeNode node4 = new TreeNode(4);
        TreeNode node9 = new TreeNode(9);
        TreeNode node2 = new TreeNode(2);

        TreeNode node3 = new TreeNode(3);
        TreeNode node10 = new TreeNode(10);
        TreeNode node6 = new TreeNode(6);
        node1.left = node5;
        node1.right = node3;

        node5.right = node4;
        node4.left = node9;
        node4.right = node2;

        node3.left = node10;
        node3.right = node6;

        BinaryTreeInfection obj = new BinaryTreeInfection();
        Assert.assertEquals(obj.amountOfTime(node1, 3), 4);
    }
}