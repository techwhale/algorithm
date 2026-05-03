package org.practise.algorithm.interestingideas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubSetII {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        Arrays.sort(nums);

        int subsetCount = 0;

        for (int i = 0; i < nums.length; i++) {
            int startIdx = (i > 0 && nums[i] == nums[i -1]) ? subsetCount : 0;
            subsetCount = result.size();
            for (int j = startIdx; j < subsetCount; j++) {
                List<Integer> temp = new ArrayList<>(result.get(j));
                temp.add(nums[i]);
                result.add(temp);
            }
        }
        return result;
    }
}
