package org.practise.algorithm.leetcode.tree.hard;

import org.practise.algorithm.pojo.TreeNode;

public class SerializeAndDeserializeTree {
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder serialized = new StringBuilder();
        return serialize(root, serialized).toString();
    }

    private StringBuilder serialize(TreeNode root, StringBuilder serialized) {
        if (root == null)
            serialized.append("null,");
        else {
            serialized.append(root.val + ",");
            serialize(root.left, serialized);
            serialize(root.right, serialized);
        }
        return serialized;
    }



    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        int[] position = new int[1];
        position[0] = 0;
        String[] serialized = data.split(",");
        return deserialize(position, serialized);
    }

    private TreeNode deserialize(int[] position, String[] serialized) {
        if (serialized[position[0]].equals("null")) {
            return null;
        }
        int value = Integer.parseInt(serialized[position[0]]);
        TreeNode node = new TreeNode(value);
        ++position[0];
        node.left = deserialize(position, serialized);
        ++position[0];
        node.right = deserialize(position, serialized);
        return node;
    }
}
