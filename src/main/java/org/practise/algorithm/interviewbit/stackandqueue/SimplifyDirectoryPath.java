package org.practise.algorithm.interviewbit.stackandqueue;

import java.util.ArrayDeque;
import java.util.Stack;

/**
 * Given an absolute path for a file (Unix-style), simplify it.
 *
 * Examples:
 *
 * path = "/home/", => "/home"
 * path = "/a/./b/../../c/", => "/c"
 * Note that absolute path always begin with ‘/’ ( root directory )
 * Path will not have whitespace characters.
 */
public class SimplifyDirectoryPath {
    public String simplifyPath(String A) {
        if (A == null || A.isEmpty()) return A;
        String[] segments = A.split("/");
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        for (int i = 0; i < segments.length; i++) {
            String path = segments[i];
            if (path == null || path.isEmpty())
                continue;
            else if (".".equals(path))
                continue;
            else if ( "..".equals(path)){
                if (! arrayDeque.isEmpty()) {
                    arrayDeque.pollLast();
                }
            } else {
                arrayDeque.offer(path);
            }
        }

        StringBuilder path = new StringBuilder();
        while(! arrayDeque.isEmpty()) {
            path.append("/").append(arrayDeque.pollFirst());
        }
        if (path.length() == 0) path.append("/");
        return path.toString();
    }
}
