package org.practise.algorithm.leetcode.contest;

import org.practise.algorithm.leetcode.string.UniqueEmails;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UniqueEmailsTest {
    private UniqueEmails uniqueEmails = new UniqueEmails();

    @Test
    public void testUniqueEmails() {
        final String[] emails = {"test.email+alex@leetcode.com", "test.e.mail+bob.cathy@leetcode.com", "testemail+david@lee.tcode.com"};
        final int count = uniqueEmails.numUniqueEmails(emails);
        Assert.assertEquals(count, 2);
    }
}