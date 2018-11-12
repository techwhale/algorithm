package org.practise.algorithm.interviewbit.tree.easy;

import org.practise.algorithm.pojo.TreeNode;

import java.util.*;

/**
 * Given a binary tree, return the inorder traversal of its nodes’ values.
 *
 * Example :
 * Given binary tree
 *
 *    1
 *     \
 *      2
 *     /
 *    3
 * return [1,3,2].
 *
 * Using recursion is not allowed.
 */
public class Traversal {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null)
            return list;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            list.add(current.val);
            current = current.right;
        }
        return list;
    }

    public List<Integer> postorderTraversal(TreeNode a) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode lastNode = null;
        while (!stack.isEmpty() || a != null) {
            if (a != null) {
                stack.push(a);
                a = a.left;
            } else {
                TreeNode peekNode = stack.peek();
                if (peekNode.right != null && lastNode != peekNode.right) {
                    a = peekNode.right;
                } else {
                    result.add(peekNode.val);
                    lastNode = stack.pop();
                }

            }
        }
        return result;
    }

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> resultList = new ArrayList<>();
        if (root == null) return resultList;
        LinkedList<TreeNode> list = new LinkedList<>();
        LinkedList<TreeNode> tempList = new LinkedList<>();
        list.offer(root);
        int index = 0;
        boolean leftToRight = true;

        while (! list.isEmpty() || !tempList.isEmpty()) {
            if (list.isEmpty()) {
                list = tempList;
                tempList = new LinkedList<>();
                index++;
                leftToRight = leftToRight ^ true;
                continue;
            }
            TreeNode node = list.pollLast();

            if (resultList.size() <= index) {
                resultList.add(new ArrayList<>());
            }
            resultList.get(index).add(node.val);

            if (leftToRight) {
                if (node.left != null)  tempList.offer(node.left);
                if (node.right != null)  tempList.offer(node.right);
            } else {
                if (node.right != null)  tempList.offer(node.right);
                if (node.left != null)  tempList.offer(node.left);
            }
        }
        return resultList;
    }


    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> resultList = new ArrayList<>();
        if (root == null) return resultList;
        ArrayDeque<TreeNode> queue = new ArrayDeque<>();
        Map<Integer, List<TreeNode>> map = new HashMap<>();
        Map<TreeNode, Integer> verticalLevelMap = new HashMap<>();
        update(queue, map, verticalLevelMap, 0, root);

        int min = 0, max = 0;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            int level = verticalLevelMap.get(node);
            int leftLevel = level - 1;
            int rightLevel = level + 1;

            if (node.left != null) {
                min = Math.min(leftLevel, min);
                update(queue, map, verticalLevelMap, leftLevel, node.left);
            }

            if (node.right != null) {
                max = Math.max(rightLevel, max);
                update(queue, map, verticalLevelMap, rightLevel, node.right);
            }
        }

        for (int i = min, j = 0; i <= max; i++, j++) {
            List<TreeNode> listNode = map.get(i);
            List<Integer> list = new ArrayList<>();
            for (TreeNode node : listNode) {
                list.add(node.val);
            }
            resultList.add(list);
        }
        return resultList;
    }

    private void update(ArrayDeque<TreeNode> queue, Map<Integer, List<TreeNode>> map, Map<TreeNode, Integer> verticalLevelMap, int level, TreeNode node) {
        verticalLevelMap.put(node, level);
        List<TreeNode> list = map.getOrDefault(level, new ArrayList<>());
        list.add(node);
        map.put(level, list);
        queue.offer(node);
    }
}
