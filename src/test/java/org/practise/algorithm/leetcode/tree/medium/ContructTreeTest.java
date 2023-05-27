package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ContructTreeTest {
    ContructTree obj = new ContructTree();

    /**
     *              3
     *         /        \
     *       9           20
     *    /    \       /     \
     *  1       2     15      7
     *
     *
     *  preorder = [3, 9 , 1,  2, 20, 15, 7]
     *  inorder =  [1, 9, 2, 3, 15, 20, 7]
     *
     */
    @Test
    public void testBuildTree() {
        int[] preorder = {3, 9 , 1,  2, 20, 15, 7};
        int[] inorder = {1, 9, 2, 3, 15, 20, 7};
        TreeNode node = obj.buildTree(preorder, inorder);
    }
}