package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a binary tree, determine if it is a complete binary tree.
 * <p>
 * Definition of a complete binary tree from Wikipedia:
 * In a complete binary tree every level, except possibly the last, is completely filled, and all nodes in the last level are as far left as possible.
 * It can have between 1 and 2h nodes inclusive at the last level h.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * <p>
 * <p>
 * Input: [1,2,3,4,5,6]
 * Output: true
 * Explanation: Every level before the last is full (ie. levels with node-values {1} and {2, 3}),
 * and all nodes in the last level ({4, 5, 6}) are as far left as possible.
 * Example 2:
 * <p>
 * <p>
 * <p>
 * Input: [1,2,3,4,5,null,7]
 * Output: false
 * Explanation: The node with value 7 isn't as far left as possible.
 * <p>
 * Note:
 * <p>
 * The tree will have between 1 and 100 nodes.
 */
public class BinaryTreeCompleteness {
    public boolean isCompleteTree(TreeNode root) {
        List<ANode> nodes = new ArrayList<>();
        nodes.add(new ANode(root, 1));
        int i = 0;
        while (i < nodes.size()) {
            ANode anode = nodes.get(i++);
            if (anode.node != null) {
                nodes.add(new ANode(anode.node.left, anode.code * 2));
                nodes.add(new ANode(anode.node.right, anode.code * 2 + 1));
            }
        }
        return nodes.get(i - 1).code == nodes.size();
    }

    class ANode {
        TreeNode node;
        int code;

        public ANode(TreeNode node, int code) {
            this.node = node;
            this.code = code;
        }
    }
}
