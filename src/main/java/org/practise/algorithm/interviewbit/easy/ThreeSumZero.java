package org.practise.algorithm.interviewbit.easy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThreeSumZero {
    public ArrayList<ArrayList<Integer>> threeSum(List<Integer> A) {
        ArrayList<ArrayList<Integer>> resultList = new ArrayList<>();
        if (A.size() < 3) return resultList;
        Collections.sort(A);
        for (int firstPtr = 0; firstPtr < A.size() - 2; firstPtr++) {
            if (firstPtr > 0 && A.get(firstPtr).intValue() == A.get(firstPtr - 1).intValue()) continue;
            int target = 0 - A.get(firstPtr), secondPtr = firstPtr + 1, thirdPtr = A.size() - 1;
            while (secondPtr < thirdPtr) {
                int sum = A.get(secondPtr) + A.get(thirdPtr);
                if (sum == target) {
                    resultList.add(new ArrayList(Arrays.asList(A.get(firstPtr), A.get(secondPtr), A.get(thirdPtr))));
                    while (secondPtr < thirdPtr && A.get(secondPtr) == A.get(secondPtr + 1)) secondPtr++;
                    while (secondPtr < thirdPtr && A.get(thirdPtr) == A.get(thirdPtr - 1)) thirdPtr--;
                    secondPtr++;
                    thirdPtr--;
                } else if (sum > target) {
                    thirdPtr--;
                } else {
                    secondPtr++;
                }
            }
        }
        return resultList;
    }
}
