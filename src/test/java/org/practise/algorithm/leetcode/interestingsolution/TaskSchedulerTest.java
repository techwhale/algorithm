package org.practise.algorithm.leetcode.interestingsolution;

import org.practise.algorithm.interestingideas.TaskScheduler;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TaskSchedulerTest {
    private TaskScheduler obj = new TaskScheduler();

    @Test
    public void testTaskScheduler() {
        char[] tasks = new char[] {'A', 'A', 'A', 'A', 'B', 'B', 'B', 'B', 'C', 'C', 'C', 'D'};
        Assert.assertEquals(obj.leastInterval(tasks, 4), 17);
    }
}