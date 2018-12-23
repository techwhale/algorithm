package org.practise.algorithm.interestingideas;

import java.util.ArrayList;
import java.util.List;

public class GrayCode {
    public List<Integer> grayCode(int n) {
        List<Integer> resultList = new ArrayList<>();
        if (n == 0) {
            resultList.add(0);
            return resultList;
        }
        resultList.add(0);
        resultList.add(1);
        int ptr = 1;

        while (ptr < n) {
            for (int i = resultList.size() - 1; i >= 0; i--) {
                resultList.add((1 << ptr) + resultList.get(i));
            }
            ptr++;
        }
        return resultList;
    }
}
