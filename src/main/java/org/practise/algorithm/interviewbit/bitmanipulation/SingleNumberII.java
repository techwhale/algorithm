package org.practise.algorithm.interviewbit.bitmanipulation;

import java.util.List;

public class SingleNumberII {
    public int singleNumber(int[] nums) {
        int singleNumber = 0;
        for (int i = 31; i >= 0; i--) {
            int count = 0;
            for (int num : nums) {
                if (((num >> i) & 1) == 1) {
                    count++;
                }
            }
            singleNumber = (singleNumber << 1) + (count%3);
        }
        return singleNumber;
    }
}
