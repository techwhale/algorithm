package org.practise.algorithm.leetcode.contest;

import java.util.*;

public class MinimumAreaRectangle {
    public int minAreaRect(int[][] points) {
        int n = points.length;
        Set<Long> set = new HashSet<>();
        for(int i = 0;i < n;i++){
            set.add((long)points[i][0]<<32|points[i][1]);
        }

        int ret = Integer.MAX_VALUE;
        for(int i = 0;i < n;i++){
            for(int j = i+1;j < n;j++){
                long S = Math.abs((long)(points[i][0]-points[j][0])*(points[i][1]-points[j][1]));
                if(S == 0)continue;
                long x = (long)points[i][0]<<32|points[j][1];
                if(!set.contains(x))continue;
                x = (long)points[j][0]<<32|points[i][1];
                if(!set.contains(x))continue;
                ret = Math.min(ret, (int)S);
            }
        }
        if(ret == Integer.MAX_VALUE)return 0;
        return ret;
    }
}
