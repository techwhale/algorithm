package org.practise.algorithm.interviewbit.linkedlist.medium;

import org.practise.algorithm.pojo.ListNode;

public class MergeKSortedList {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) return null;
        return sort(lists, 0, lists.length - 1);
    }

    public ListNode sort(ListNode[] lists, int l, int r) {
        if (l == r) return lists[l];
        int mid = (r - l) / 2 + l;
        ListNode left = sort(lists, l, mid);
        ListNode right = sort(lists, mid + 1, r);
        return merge(left, right);
    }

    public ListNode merge(ListNode left, ListNode right) {
        ListNode dummy = new ListNode(0), ptr = dummy;
        while(left != null && right != null) {
            if (left.val < right.val) {
                ptr.next = left;
                left = left.next;
            } else {
                ptr.next = right;
                right = right.next;
            }
            ptr = ptr.next;
        }

        if (left == null) ptr.next = right;
        if (right == null) ptr.next = left;
        return dummy.next;
    }
}
