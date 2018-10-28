package org.practise.algorithm.leetcode.array;

public class BinarySubarraysWithSum {
    public int numSubarraysWithSum(int[] a, int S) {
        int n = a.length;
        int[] cum = new int[n+1];
        for(int i = 0;i < n;i++)cum[i+1] = cum[i] + a[i];

        int ret = 0;
        int[] f = new int[30003];
        for(int i = 0;i <= n;i++){
            if(cum[i]-S >= 0)ret += f[cum[i]-S];
            f[cum[i]]++;
        }
        return ret;
    }
}
