package org.practise.algorithm.interviewbit.tree.easy;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class TraversalTest {
    private Traversal traversal = new Traversal();
    @Test
    public void testVerticalOrder() {
        //3,9,20,null,null,15,7
        TreeNode node3 = new TreeNode(3);
        TreeNode node9 = new TreeNode(9);
        TreeNode node20 = new TreeNode(20);
        TreeNode node15 = new TreeNode(15);
        TreeNode node7 = new TreeNode(7);
        node3.left = node9;
        node3.right = node20;
        node20.left = node15;
        node20.right = node7;

        final List<List<Integer>> resultList = traversal.verticalOrder(node3);
        Assert.assertEquals(resultList.toString(), "[[9], [3, 15], [20], [7]]");
    }

    @Test
    public void testZigZagOrder() {
        //3,9,20,null,null,15,7
        TreeNode node3 = new TreeNode(3);
        TreeNode node9 = new TreeNode(9);
        TreeNode node20 = new TreeNode(20);
        TreeNode node15 = new TreeNode(15);
        TreeNode node7 = new TreeNode(7);

        TreeNode node8 = new TreeNode(8);
        TreeNode node10 = new TreeNode(10);

        node3.left = node9;
        node3.right = node20;

        node9.left = node8;
        node9.right = node10;

        node20.left = node15;
        node20.right = node7;

        final List<List<Integer>> resultList = traversal.zigzagLevelOrder(node3);
        Assert.assertEquals(resultList.get(2), Arrays.asList(8, 10, 15, 7));
        Assert.assertEquals(resultList.toString(), "[[3], [20, 9], [8, 10, 15, 7]]");
    }
}