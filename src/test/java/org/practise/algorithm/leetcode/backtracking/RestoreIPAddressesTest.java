package org.practise.algorithm.leetcode.backtracking;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class RestoreIPAddressesTest {
    private RestoreIPAddresses obj = new RestoreIPAddresses();

    @Test
    public void testRestoreIPAddresses() {
        String s = "25525511135";
        final List<String> ipAddresses = obj.restoreIpAddresses(s);
        Assert.assertEquals(ipAddresses.size(), 2);
        Assert.assertTrue(ipAddresses.contains("255.255.11.135"));
        Assert.assertTrue(ipAddresses.contains("255.255.111.35"));
    }
}