package org.practise.algorithm.leetcode.array.hard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2040. Kth Smallest Product of Two Sorted Arrays
 * Hard
 * Topics
 * Companies
 * Hint
 * Given two sorted 0-indexed integer arrays nums1 and nums2 as well as an integer k, return the kth (1-based) smallest product of nums1[i] * nums2[j] where 0 <= i < nums1.length and 0 <= j < nums2.length.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [2,5], nums2 = [3,4], k = 2
 * Output: 8
 * Explanation: The 2 smallest products are:
 * - nums1[0] * nums2[0] = 2 * 3 = 6
 * - nums1[0] * nums2[1] = 2 * 4 = 8
 * The 2nd smallest product is 8.
 * Example 2:
 *
 * Input: nums1 = [-4,-2,0,3], nums2 = [2,4], k = 6
 * Output: 0
 * Explanation: The 6 smallest products are:
 * - nums1[0] * nums2[1] = (-4) * 4 = -16
 * - nums1[0] * nums2[0] = (-4) * 2 = -8
 * - nums1[1] * nums2[1] = (-2) * 4 = -8
 * - nums1[1] * nums2[0] = (-2) * 2 = -4
 * - nums1[2] * nums2[0] = 0 * 2 = 0
 * - nums1[2] * nums2[1] = 0 * 4 = 0
 * The 6th smallest product is 0.
 * Example 3:
 *
 * Input: nums1 = [-2,-1,0,1,2], nums2 = [-3,-1,2,4,5], k = 3
 * Output: -6
 * Explanation: The 3 smallest products are:
 * - nums1[0] * nums2[4] = (-2) * 5 = -10
 * - nums1[0] * nums2[3] = (-2) * 4 = -8
 * - nums1[4] * nums2[0] = 2 * (-3) = -6
 * The 3rd smallest product is -6.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 5 * 104
 * -105 <= nums1[i], nums2[j] <= 105
 * 1 <= k <= nums1.length * nums2.length
 * nums1 and nums2 are sorted.
 */
public class KthSmallestProductTwoSortedArrays {
    public long kthSmallestProduct2(int[] nums1, int[] nums2, long k) {
        List<Integer> A1 = new ArrayList<>(); // minus
        List<Integer> A2 = new ArrayList<>(); // positive
        List<Integer> B1 = new ArrayList<>();
        List<Integer> B2 = new ArrayList<>(); // positive

        classifyPositiveAndNegative(nums1, A2, A1);
        classifyPositiveAndNegative(nums2, B2, B1);

        Collections.sort(A1);
        Collections.sort(B1);

        int negativeSize =
                A2.size() * B1.size()
                        + A1.size() * B2.size();

        int sign = 1;
        if (k > negativeSize) {
            k = k - negativeSize;
        } else {
            sign = -1;
            k = negativeSize - k + 1;
            List<Integer> temp = B2;
            B2 = B1;
            B1 = temp;
        }
        long left = 0;
        long right = (long)1e10+1;
        while (left < right) {
            long mid = (left + right) / 2;
            if (k <= (check2(mid, A1, B1) + check2(mid, A2, B2))) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left * sign;
    }

        private long check2(long value, List<Integer> nums1, List<Integer> nums2) {
        long total = 0;
        int j = nums2.size() - 1;

        for(Integer n1 : nums1) {
            while(j >= 0 && (long)n1 * (long)nums2.get(j) > value) {
                j--;
            }

            total += j + 1;
        }

        return total;
    }

    private void classifyPositiveAndNegative(int[] numArray, List<Integer> positiveList, List<Integer> negativeList) {
        for (int num : numArray) {
            if (num < 0) {
                negativeList.add(num * -1);
            } else {
                positiveList.add(num);
            }
        }
    }

    public long kthSmallestProduct(int[] nums1, int[] nums2, long k) {
        long left = (long)0;
        long right = (long)1e10 + 1;
        long negativeSize = 0;
        List<Integer> A1 = new ArrayList<>(); // minus
        List<Integer> A2 = new ArrayList<>(); // plus
        List<Integer> B1 = new ArrayList<>(); // minus
        List<Integer> B2 = new ArrayList<>(); // plus
        long s = 1;

        for(int i=0; i < nums1.length; i++) {
            if(nums1[i] < 0) {
                A1.add(nums1[i] * -1);
            }

            if(nums1[i] >= 0) {
                A2.add(nums1[i]);
            }
        }

        for(int i=0; i < nums2.length; i++) {
            if(nums2[i] < 0) {
                B1.add(nums2[i] * -1);
            }

            if(nums2[i] >= 0) {
                B2.add(nums2[i]);
            }
        }

        Collections.sort(A1);
        Collections.sort(B1);


        negativeSize = A1.size() * B2.size() + A2.size() * B1.size();

        if(k > negativeSize) {
            k -= negativeSize;
            s = 1;
        } else {
            k = negativeSize - k + 1;
            List<Integer> temp = B1;
            B1 = B2;
            B2 = temp;
            s = -1;
        }


        while(left < right) {
            long mid = left + (right - left) / 2;

            if(k <= check(mid, A1, B1) + check(mid, A2, B2)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left * s;
    }

    private long check(long value, List<Integer> nums1, List<Integer> nums2) {
        long total = 0;
        int j = nums2.size() - 1;

        for(Integer n1 : nums1) {
            while(j >= 0 && (long)n1 * (long)nums2.get(j) > value) {
                j--;
            }

            total += j + 1;
        }

        return total;
    }
}
