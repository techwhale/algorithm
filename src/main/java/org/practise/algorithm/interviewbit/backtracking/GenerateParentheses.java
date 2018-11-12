package org.practise.algorithm.interviewbit.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses of length 2*n.
 *
 * For example, given n = 3, a solution set is:
 *
 * "((()))", "(()())", "(())()", "()(())", "()()()"
 * Make sure the returned list of strings are sorted.
 */
public class GenerateParentheses {
    public void wrapper(List<String> res, char[] arr, int open, int close, int i, int n){
        if(close == n){
            res.add(new String(arr));
            return;
        }
        if(open<n){
            arr[i]='(';
            wrapper(res,arr,open+1,close,i+1,n);
        }
        if(close < open){
            arr[i]=')';
            wrapper(res, arr,open,close+1,i+1,n);
        }
    }
    public List<String> generateParenthesis(int n) {
        List<String> res= new ArrayList<String>();
        if(n == 0){
            return res;
        }
        char[] arr = new char[2*n];
        wrapper(res, arr, 0, 0, 0, n);
        return res;
    }
}
