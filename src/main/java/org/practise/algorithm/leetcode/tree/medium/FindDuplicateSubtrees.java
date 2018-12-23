package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Given a binary tree, return all duplicate subtrees.
 * For each kind of duplicate subtrees, you only need to return the root node of any one of them.
 *
 * Two trees are duplicate if they have the same structure with same node values.
 *
 * Example 1:
 *
 *         1
 *        / \
 *       2   3
 *      /   / \
 *     4   2   4
 *        /
 *       4
 * The following are two duplicate subtrees:
 *
 *       2
 *      /
 *     4
 * and
 *
 *     4
 * Therefore, you need to return above trees' root in the form of a list.
 */
public class FindDuplicateSubtrees {
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        Map<String, Integer> mapTreeCount = new HashMap<>();
        List<TreeNode> result = new ArrayList<>();
        addDuplicateSubTreeIfExist(root, mapTreeCount, result);
        return result;
    }

    private String addDuplicateSubTreeIfExist(TreeNode node, Map<String, Integer> mapTreeCount, List<TreeNode> result){
        if (node == null)
            return "#";
        String tree = node.val + addDuplicateSubTreeIfExist(node.left, mapTreeCount, result) + addDuplicateSubTreeIfExist(node.right, mapTreeCount, result);
        mapTreeCount.put(tree, mapTreeCount.getOrDefault(tree, 0) + 1);
        if (mapTreeCount.get(tree) == 2) {
            result.add(node);
        }
        return tree;
    }
}
