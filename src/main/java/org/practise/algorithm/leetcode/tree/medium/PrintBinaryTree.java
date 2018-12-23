package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Print a binary tree in an m*n 2D string array following these rules:
 *
 * The row number m should be equal to the height of the given binary tree.
 * The column number n should always be an odd number.
 * The root node's value (in string format) should be put in the exactly middle of the first row it can be put.
 * The column and the row where the root node belongs will separate the rest space into two parts (left-bottom part and right-bottom part).
 * You should print the left subtree in the left-bottom part and print the right subtree in the right-bottom part.
 * The left-bottom part and the right-bottom part should have the same size. Even if one subtree is none while the other is not,
 * you don't need to print anything for the none subtree but still need to leave the space as large as that for the other subtree.
 * However, if two subtrees are none, then you don't need to leave space for both of them.
 * Each unused space should contain an empty string "".
 * Print the subtrees following the same rules.
 * Example 1:
 * Input:
 *      1
 *     /
 *    2
 * Output:
 * [["", "1", ""],
 *  ["2", "", ""]]
 * Example 2:
 * Input:
 *      1
 *     / \
 *    2   3
 *     \
 *      4
 * Output:
 * [["", "", "", "1", "", "", ""],
 *  ["", "2", "", "", "", "3", ""],
 *  ["", "", "4", "", "", "", ""]]
 * Example 3:
 * Input:
 *       1
 *      / \
 *     2   5
 *    /
 *   3
 *  /
 * 4
 * Output:
 *
 * [["",  "",  "", "",  "", "", "", "1", "",  "",  "",  "",  "", "", ""]
 *  ["",  "",  "", "2", "", "", "", "",  "",  "",  "",  "5", "", "", ""]
 *  ["",  "3", "", "",  "", "", "", "",  "",  "",  "",  "",  "", "", ""]
 *  ["4", "",  "", "",  "", "", "", "",  "",  "",  "",  "",  "", "", ""]]
 * Note: The height of binary tree is in the range of [1, 10].
 */
public class PrintBinaryTree {
    public List<List<String>> printTree(TreeNode root) {
        if (root == null)
            return Collections.EMPTY_LIST;
        int depth = findDepth(root), size = (int) (Math.pow(2, depth) - 1);
        List<List<String>> treeList = new ArrayList<>();
        for (int i = 0; i < depth; i++) {
            String[] array = new String[size];
            Arrays.fill(array, "");
            treeList.add(Arrays.asList(array));
        }
        fillTree(root, 0, 0, size, treeList);
        return treeList;
    }

    private void fillTree(TreeNode node, int depth, int start, int end, List<List<String>> treeList) {
        if (node == null)
            return;
        int mid = (start + end) / 2;
        List<String> level = treeList.get(depth);
        level.set(mid, String.valueOf(node.val));
        fillTree(node.left, depth + 1, start, mid - 1, treeList);
        fillTree(node.right, depth + 1, mid + 1, end, treeList);
    }

    private int findDepth(TreeNode node) {
        if (node == null)
            return 0;
        return Math.max(findDepth(node.left), findDepth(node.right)) + 1;
    }
}
