package org.practise.algorithm.leetcode.linkedlist;

import org.practise.algorithm.pojo.ListNode;

/**
 * Given a linked list, reverse the nodes of a linked list k at a time and return its modified list.
 *
 * k is a positive integer and is less than or equal to the length of the linked list.
 * If the number of nodes is not a multiple of k then left-out nodes in the end should remain as it is.
 *
 * Example:
 *
 * Given this linked list: 1->2->3->4->5
 *
 * For k = 2, you should return: 2->1->4->3->5
 *
 * For k = 3, you should return: 3->2->1->4->5
 *
 * Note:
 *
 * Only constant extra memory is allowed.
 * You may not alter the values in the list's nodes, only nodes itself may be changed.
 */
public class ReverseNodesInKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (k < 2 || head == null) return head;
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode prev = dummy, tail = dummy, temp;

        while (tail != null) {
            int count = k;
            while (count > 0 && tail != null) {
                count--;
                tail = tail.next;
            }

            if (tail == null) break;

            head = prev.next;

            while (prev.next != tail) {
                temp = prev.next;
                prev.next = temp.next;
                temp.next = tail.next;
                tail.next = temp;
            }

            prev = head;
            tail = head;
        }
        return dummy.next;
    }
}
