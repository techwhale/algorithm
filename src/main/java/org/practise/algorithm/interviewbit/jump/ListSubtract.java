package org.practise.algorithm.interviewbit.jump;

import org.practise.algorithm.pojo.ListNode;

/**
 *Given a singly linked list, modify the value of first half nodes such that :
 *
 * 1st node’s new value = the last node’s value - first node’s current value
 * 2nd node’s new value = the second last node’s value - 2nd node’s current value,
 * and so on …
 *
 *  NOTE :
 * If the length L of linked list is odd, then the first half implies at first floor(L/2) nodes. So, if L = 5, the first half refers to first 2 nodes.
 * If the length L of linked list is even, then the first half implies at first L/2 nodes. So, if L = 4, the first half refers to first 2 nodes.
 * Example :
 *
 * Given linked list 1 -> 2 -> 3 -> 4 -> 5,
 *
 * You should return 4 -> 2 -> 3 -> 4 -> 5
 * as
 *
 * for first node, 5 - 1 = 4
 * for second node, 4 - 2 = 2
 * Try to solve the problem using constant extra space.
 */
public class ListSubtract {
    public ListNode subtract(ListNode node) {
        if (node == null || node.next == null) return node;
        ListNode dummyHead = new ListNode(0), dummyTail = new ListNode(0);
        dummyHead.next = node;
        ListNode middleNode = findMiddleNode(dummyHead);

        // reverse second half
        ListNode prev = null;
        while (middleNode != null) {
            ListNode temp = middleNode.next;
            middleNode.next = prev;
            prev = middleNode;
            middleNode = temp;
        }

        dummyTail.next = prev;

        ListNode pre = dummyHead, post = dummyTail;
        while (pre != null && pre != post) {
            int diff = post.val - pre.val;
            pre.val = diff;
            post = post.next;
            pre = pre.next;
        }

        // reverse the second half back;
        prev = dummyTail;
        ListNode tailNode = dummyTail.next, lastNode = dummyTail.next;
        while (tailNode != null) {
            ListNode temp = tailNode.next;
            tailNode.next = prev;
            prev = tailNode;
            tailNode = temp;
        }
        lastNode.next = null;

        return dummyHead.next;
    }

    private ListNode findMiddleNode(ListNode node) {
        ListNode slowPtr = node, fastPtr = node;
        while (fastPtr != null && fastPtr.next != null) {
            slowPtr = slowPtr.next;
            fastPtr = fastPtr.next.next;
        }
        return slowPtr;
    }
}
