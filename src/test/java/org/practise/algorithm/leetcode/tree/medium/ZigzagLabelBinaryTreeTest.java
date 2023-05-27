package org.practise.algorithm.leetcode.tree.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class ZigzagLabelBinaryTreeTest {
    private ZigzagLabelBinaryTree obj = new ZigzagLabelBinaryTree();
    @Test
    public void testPathInZigZagTree() {
        Assert.assertEquals(obj.pathInZigZagTree(14), Arrays.asList(1,3,4,14));
        Assert.assertEquals(obj.pathInZigZagTree(26), Arrays.asList(1,2,6,10,26));
    }
}