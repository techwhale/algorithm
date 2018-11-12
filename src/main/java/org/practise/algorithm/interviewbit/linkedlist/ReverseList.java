package org.practise.algorithm.interviewbit.linkedlist;

import org.practise.algorithm.pojo.ListNode;

public class ReverseList {
    public ListNode reverseList(ListNode node) {
        if (node == null) return node;
        ListNode prev = null;
        while (node != null) {
            ListNode temp = node.next;
            node.next = prev;
            prev = node;
            node = temp;
        }
        return prev;
    }
}
