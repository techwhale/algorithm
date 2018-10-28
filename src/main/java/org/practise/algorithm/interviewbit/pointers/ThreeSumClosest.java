package org.practise.algorithm.interviewbit.pointers;

import java.util.Collections;
import java.util.List;

public class ThreeSumClosest {
    public int threeSumClosest(List<Integer> A, int B) {
        if (A == null || A.size() < 3) return -1; // invalid case
        Collections.sort(A);
        long closestSum = Long.MAX_VALUE;
        for (int firstIndex = 0; firstIndex < A.size() - 2; firstIndex++) {
            int secondIndex = firstIndex + 1, thirdIndex = A.size() - 1;
            while (secondIndex < thirdIndex) {
                int sum = A.get(firstIndex) + A.get(secondIndex) + A.get(thirdIndex);

                if (Math.abs(sum - B) <= Math.abs(closestSum - B)) {
                    closestSum = sum;
                }

                if (B > sum) secondIndex++;
                else thirdIndex--;
            }
        }
        return (int) closestSum;
    }
}
