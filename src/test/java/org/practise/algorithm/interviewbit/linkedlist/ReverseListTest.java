package org.practise.algorithm.interviewbit.linkedlist;

import org.practise.algorithm.pojo.ListNode;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReverseListTest {
    private ReverseList obj = new ReverseList();

    @Test
    public void testReverseList() {
        ListNode node1 = new ListNode(1), node2 = new ListNode(2), node3 = new ListNode(3), node4 = new ListNode(4);
        node1.next = node2; node2.next = node3; node3.next = node4;

        ListNode reversedList = obj.reverseList(node1);

        int val = 4;
        while (reversedList != null) {
            Assert.assertEquals(reversedList.val, val--);
            reversedList = reversedList.next;
        }
    }
}