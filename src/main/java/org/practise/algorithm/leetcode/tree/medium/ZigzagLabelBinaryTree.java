package org.practise.algorithm.leetcode.tree.medium;

import java.util.LinkedList;
import java.util.List;

/**
 * 1104. Path In Zigzag Labelled Binary Tree
 * Medium
 * In an infinite binary tree where every node has two children, the nodes are labelled in row order.
 * In the odd numbered rows (ie., the first, third, fifth,...), the labelling is left to right, while in the even numbered rows (second, fourth, sixth,...), the labelling is right to left.
 * Given the label of a node in this tree, return the labels in the path from the root of the tree to the node with that label.
 *
 * Example 1:
 * Input: label = 14
 * Output: [1,3,4,14]
 *
 * Example 2:
 * Input: label = 26
 * Output: [1,2,6,10,26]
 *
 * Constraints:
 * 1 <= label <= 10^6
 */
public class ZigzagLabelBinaryTree {
    public List<Integer> pathInZigZagTree(int label) {
        LinkedList<Integer> result = new LinkedList<>();
        int parent = label;
        result.addFirst(parent);

        while(parent != 1) {
            int depth = (int)(Math.log(parent)/Math.log(2));
            int offset = (int)Math.pow(2, depth + 1) - 1 - parent;
            parent = ((int)Math.pow(2, depth) + offset) / 2;
            result.addFirst(parent);
        }

        return result;
    }
}
