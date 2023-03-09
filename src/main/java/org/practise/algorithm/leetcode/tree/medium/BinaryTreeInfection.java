package org.practise.algorithm.leetcode.tree.medium;

import org.practise.algorithm.pojo.TreeNode;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * 2385. Amount of Time for Binary Tree to Be Infected
 * <p>
 * You are given the root of a binary tree with unique values, and an integer start. At minute 0, an infection starts from the node with value start.
 * <p>
 * Each minute, a node becomes infected if:
 * <p>
 * The node is currently uninfected.
 * The node is adjacent to an infected node.
 * Return the number of minutes needed for the entire tree to be infected.
 * <p>
 * <p>
 * Input: root = [1,5,3,null,4,10,6,9,2], start = 3
 * Output: 4
 * Explanation: The following nodes are infected during:
 * - Minute 0: Node 3
 * - Minute 1: Nodes 1, 10 and 6
 * - Minute 2: Node 5
 * - Minute 3: Node 4
 * - Minute 4: Nodes 9 and 2
 * It takes 4 minutes for the whole tree to be infected so we return 4.
 * <p>
 * Input: root = [1], start = 1
 * Output: 0
 * Explanation: At minute 0, the only node in the tree is infected so we return 0.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * The number of nodes in the tree is in the range [1, 105].
 * 1 <= Node.val <= 105
 * Each node has a unique value.
 * A node with a value of start exists in the tree.
 */
public class BinaryTreeInfection {
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode() {}
     * TreeNode(int val) { this.val = val; }
     * TreeNode(int val, TreeNode left, TreeNode right) {
     * this.val = val;
     * this.left = left;
     * this.right = right;
     * }
     * }
     */
    Map<Integer, List<Integer>> graph;
    Set<Integer> visited;

    private void buildGraph(TreeNode node) {
        graph.put(node.val, new ArrayList<>());
        if (node.left != null) {
            buildGraph(node.left);
            graph.get(node.left.val).add(node.val);
            graph.get(node.val).add(node.left.val);
        }
        if (node.right != null) {
            buildGraph(node.right);
            graph.get(node.right.val).add(node.val);
            graph.get(node.val).add(node.right.val);
        }
    }

    private int dfs(int start) {
        if (!visited.add(start)) {
            return 0;
        }
        int result = 1;
        for (int child : graph.get(start)) {
            result = Math.max(result, 1 + dfs(child));
        }
        return result;
    }

    public int amountOfTime(TreeNode root, int start) {
        if (root == null) {
            return 0;
        }
        graph = new HashMap<>();
        visited = new HashSet<>();
        buildGraph(root);
        return dfs(start) - 1; // eliminate the infected node
    }
}
