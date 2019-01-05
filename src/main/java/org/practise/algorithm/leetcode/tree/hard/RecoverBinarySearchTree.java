package org.practise.algorithm.leetcode.tree.hard;

import org.practise.algorithm.pojo.TreeNode;

public class RecoverBinarySearchTree {
    public void recoverTree(TreeNode root) {
        TreeNode current = root, previous = null, first = null, second = null;
        while(current != null) {
            //left is null then print the node and go to right
            if (current.left == null) {
                if (previous != null && previous.val > current.val) {
                    if (first == null) { first = previous; second = current;}
                    else {second = current;}
                }
                previous = current;
                current = current.right;
            }
            else {
                //find the predecessor.
                TreeNode predecessor = current.left;
                //To find predecessor keep going right till right node is not null or right node is not current.
                while(predecessor.right != current && predecessor.right != null){
                    predecessor = predecessor.right;
                }
                //if right node is null then go left after establishing link from predecessor to current.
                if(predecessor.right == null){
                    predecessor.right = current;
                    current = current.left;
                }else{ //left is already visit. Go right after visiting current.
                    if (previous != null && previous.val > current.val) {
                        if (first == null) { first = previous; second = current;}
                        else {second = current;}
                    }
                    previous = current;
                    predecessor.right = null;
                    current = current.right;
                }
            }
        }
        swap(first, second);
    }

    private void swap(TreeNode node1, TreeNode node2) {
        int temp = node1.val;
        node1.val = node2.val;
        node2.val = temp;
    }
}
