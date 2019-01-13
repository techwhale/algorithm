package org.practise.algorithm.leetcode.string.easy;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Given an absolute path for a file (Unix-style), simplify it.
 *
 * For example,
 * path = "/home/", => "/home"
 * path = "/a/./b/../../c/", => "/c"
 * path = "/a/../../b/../c//.//", => "/c"
 * path = "/a//b////c/d//././/..", => "/a/b/c"
 *
 * In a UNIX-style file system, a period ('.') refers to the current directory, so it can be ignored in a simplified path.
 * Additionally, a double period ("..") moves up a directory, so it cancels out whatever the last directory was. For more information,
 * look here: https://en.wikipedia.org/wiki/Path_(computing)#Unix_style
 *
 * Corner Cases:
 *
 * Did you consider the case where path = "/../"?
 * In this case, you should return "/".
 * Another corner case is the path might contain multiple slashes '/' together, such as "/home//foo/".
 * In this case, you should ignore redundant slashes and return "/home/foo".
 */
public class SimplifyPath {
    public String simplifyPath(String path) {
        String[] split = path.split("/");
        Deque<String> queue = new LinkedList<>();
        for (String directory : split) {
            if (directory.equals("..")) {
                if (! queue.isEmpty()) {
                    queue.pollLast();
                }
            } else if (directory.equals(".") || directory.isEmpty()) {
                continue;
            } else {
                queue.offerLast(directory);
            }
        }

        StringBuilder builder = new StringBuilder();
        while (! queue.isEmpty()) {
            builder.append("/");
            builder.append(queue.pollFirst());
        }
        return builder.length() == 0 ? "/" : builder.toString();
    }
}
