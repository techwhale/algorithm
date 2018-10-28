package org.practise.algorithm.interviewbit.stackandqueue;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SimplifyDirectoryPathTest {
    private SimplifyDirectoryPath simplifyDirectoryPath = new SimplifyDirectoryPath();

    @Test
    public void testSimplifyDirectoryPath() {
        String path = "/";
        Assert.assertEquals(simplifyDirectoryPath.simplifyPath(path), "/");
    }

    @Test
    public void testSimplifyDirectoryPath2() {
        String path = "/home/";
        Assert.assertEquals(simplifyDirectoryPath.simplifyPath(path), "/home");
    }

    @Test
    public void testSimplifyDirectoryPath3() {
        String path = "/a/./b/../../c/";
        Assert.assertEquals(simplifyDirectoryPath.simplifyPath(path), "/c");
    }

    @Test
    public void testSimplifyDirectoryPath4() {
        String path = "/a/./b/../../c/d";
        Assert.assertEquals(simplifyDirectoryPath.simplifyPath(path), "/c/d");
    }

    @Test
    public void testSimplifyDirectoryPath5() {
        String path = "/../";
        Assert.assertEquals(simplifyDirectoryPath.simplifyPath(path), "/");
    }

}