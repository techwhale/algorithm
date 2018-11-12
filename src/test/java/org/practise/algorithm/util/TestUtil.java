package org.practise.algorithm.util;

import org.practise.algorithm.pojo.ListNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtil {
    public static ListNode createListNode(int[] arr) {
        ListNode dummyHead = new ListNode(0);
        ListNode prev = dummyHead;
        for (int a : arr) {
            final ListNode node = new ListNode(a);
            prev.next = node;
            prev = node;
        }
        return dummyHead.next;
    }

    public static List<Integer> convertLinkedListToList(ListNode node) {
        if (node == null) return Collections.emptyList();
        List<Integer> list = new ArrayList<>();
        while (node != null) {
            list.add(node.val);
            node = node.next;
        }
        return list;
    }
}
