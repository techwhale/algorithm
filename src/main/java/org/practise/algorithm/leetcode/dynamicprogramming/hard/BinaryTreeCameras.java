package org.practise.algorithm.leetcode.dynamicprogramming.hard;

import org.practise.algorithm.pojo.TreeNode;

/**
 *
 * 968. Binary Tree Cameras
 *
 * Given a binary tree, we install cameras on the nodes of the tree.
 *
 * Each camera at a node can monitor its parent, itself, and its immediate children.
 *
 * Calculate the minimum number of cameras needed to monitor all nodes of the tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: [0,0,null,0,0]
 * Output: 1
 * Explanation: One camera is enough to monitor all nodes if placed as shown.
 * Example 2:
 *
 *
 * Input: [0,0,null,0,null,0,null,null,0]
 * Output: 2
 * Explanation: At least two cameras are needed to monitor all nodes of the tree. The above image shows one of the valid configurations of camera placement.
 *
 * Note:
 *
 * The number of nodes in the given tree will be in the range [1, 1000].
 * Every node has value 0.
 */
public class BinaryTreeCameras {
    public int minCameraCover(TreeNode root) {
        int[] result = solve(root);
        return Math.min(result[1], result[2]);
    }

    private int[] solve(TreeNode node) {
        if (node == null) {
            return new int[] {0, 0, 9999999};
        }

        int[] left = solve(node.left), right = solve(node.right);
        int minimumCameraToCoverLeft = Math.min(left[1], left[2]),
                minimumCameraToCoverRight = Math.min(right[1], right[2]);

        int d0 = left[1] + right[1];
        int d1 = Math.min(left[2] + minimumCameraToCoverRight, right[2] + minimumCameraToCoverLeft);
        int d2 = 1 + Math.min(left[0], minimumCameraToCoverLeft) + Math.min(right[0], minimumCameraToCoverRight);
        return new int[] {d0, d1, d2};
    }
}
