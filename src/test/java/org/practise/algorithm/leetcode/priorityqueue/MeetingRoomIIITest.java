package org.practise.algorithm.leetcode.priorityqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MeetingRoomIIITest {
    private MeetingRoomIII obj = new MeetingRoomIII();
    @Test
    public void testMostBooked() {
        Assert.assertEquals(obj.mostBooked(3, new int[][]{{1,20}, {2,10}, {3,5}, {4,9}, {6,8}}), 1);
    }
}