package org.practise.algorithm.interestingideas;

import org.practise.algorithm.pojo.TreeNode;

import java.util.*;

/**
 * 272. Closest Binary Search Tree Value II
 * Hard
 * Topics
 * Companies
 * Hint
 * Given the root of a binary search tree, a target value, and an integer k, return the k values in the BST that are closest to the target. You may return the answer in any order.
 *
 * You are guaranteed to have only one unique set of k values in the BST that are closest to the target.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [4,2,5,1,3], target = 3.714286, k = 2
 * Output: [4,3]
 * Example 2:
 *
 * Input: root = [1], target = 0.000000, k = 1
 * Output: [1]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is n.
 * 1 <= k <= n <= 104.
 * 0 <= Node.val <= 109
 * -109 <= target <= 109
 *
 *
 * Follow up: Assume that the BST is balanced. Could you solve it in less than O(n) runtime (where n = total nodes)?
 */
public class KClosestBinarySearchTreeValueII {
    // Deque - O(n)
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        Deque<Integer> deque = new LinkedList<>();
        dfs(root, deque, target, k);
        return new ArrayList<>(deque);
    }

    private void dfs(TreeNode node, Deque<Integer> deque, double target, int k) {
        if (node == null) {
            return;
        }
        dfs(node.left, deque, target, k);
        deque.offerLast(node.val);
        if (deque.size() > k) {
            if (Math.abs(deque.peekLast() - target) > Math.abs(deque.peekFirst() - target)) {
                deque.pollLast();
                return;
            } else {
                deque.pollFirst();
            }
        }
        dfs(node.right, deque, target, k);
    }

    // custom sorting solution - nlogn
    public List<Integer> closestKValues1(TreeNode root, double target, int k) {
        List<Integer> values = new ArrayList<>();
        dfs1(root, values);
        Collections.sort(values, (a, b) -> Math.abs(a - target) <= Math.abs(b - target) ? -1: 1);
        return values.subList(0, k);
    }

    private void dfs1(TreeNode node, List<Integer> values) {
        if (node == null) {
            return;
        }
        values.add(node.val);
        dfs1(node.left, values);
        dfs1(node.right, values);
    }


    // heap solution - nlogk
    public List<Integer> closestKValues2(TreeNode root, double target, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> Math.abs(a - target) > Math.abs(b - target) ? -1: 1);
        dfs2(root, heap, k);
        return new ArrayList<>(heap);
    }

    private void dfs2(TreeNode node, PriorityQueue<Integer> heap, int k) {
        if (node == null) {
            return;
        }
        heap.add(node.val);
        if (heap.size() > k) {
            heap.poll();
        }
        dfs2(node.left, heap, k);
        dfs2(node.right, heap, k);
    }

    // Binary search - O(n) + O(log(n - k)); space - O(n)
    public List<Integer> closestKValues3(TreeNode root, double target, int k) {
        List<Integer> values = new ArrayList<>();
        dfs3(root, values);
        int left = 0;
        int right = values.size() - k;
        while (left < right) {
            int mid = (left + right) / 2;
            if (Math.abs(values.get(mid) - target)    >  Math.abs(values.get(mid + k) - target)) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return values.subList(left, left + k);
    }

    private void dfs3(TreeNode node, List<Integer> values) {
        if (node == null) {
            return;
        }
        dfs3(node.left, values);
        values.add(node.val);
        dfs3(node.right, values);
    }
}
