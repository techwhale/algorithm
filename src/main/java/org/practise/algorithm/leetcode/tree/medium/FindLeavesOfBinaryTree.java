package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 366. Find Leaves of Binary Tree
 *
 * Given a binary tree, collect a tree's nodes as if you were doing this: Collect and remove all leaves, repeat until the tree is empty.
 *
 *
 *
 * Example:
 *
 * Input: [1,2,3,4,5]
 *
 *           1
 *          / \
 *         2   3
 *        / \
 *       4   5
 *
 * Output: [[4,5,3],[2],[1]]
 *
 *
 * Explanation:
 *
 * 1. Removing the leaves [4,5,3] would result in this tree:
 *
 *           1
 *          /
 *         2
 *
 *
 * 2. Now removing the leaf [2] would result in this tree:
 *
 *           1
 *
 *
 * 3. Now removing the leaf [1] would result in the empty tree:
 *
 *           []
 */
public class FindLeavesOfBinaryTree {
    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> resultList = new ArrayList<>();
        findLeaves(root, resultList);
        return resultList;
    }

    private int findLeaves(TreeNode node, List<List<Integer>> resultList) {
        if (node == null)
            return -1;
        int level = 1 + Math.max(findLeaves(node.left, resultList), findLeaves(node.right, resultList));
        if (resultList.size() - 1 < level) {
            resultList.add(new ArrayList<Integer>());
        }
        resultList.get(level).add(node.val);
        return level;
    }
}
