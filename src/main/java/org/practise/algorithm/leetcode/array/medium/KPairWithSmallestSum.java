package org.practise.algorithm.leetcode.array.medium;

import java.util.*;

/**
 * 373. Find K Pairs with Smallest Sums
 * Medium
 * You are given two integer arrays nums1 and nums2 sorted in ascending order and an integer k.
 * Define a pair (u, v) which consists of one element from the first array and one element from the second array.
 * Return the k pairs (u1, v1), (u2, v2), ..., (uk, vk) with the smallest sums.
 *
 * Example 1:
 * Input: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
 * Output: [[1,2],[1,4],[1,6]]
 * Explanation: The first 3 pairs are returned from the sequence: [1,2],[1,4],[1,6],[7,2],[7,4],[11,2],[7,6],[11,4],[11,6]
 *
 * Example 2:
 * Input: nums1 = [1,1,2], nums2 = [1,2,3], k = 2
 * Output: [[1,1],[1,1]]
 * Explanation: The first 2 pairs are returned from the sequence: [1,1],[1,1],[1,2],[2,1],[1,2],[2,2],[1,3],[1,3],[2,3]
 *
 * Example 3:
 * Input: nums1 = [1,2], nums2 = [3], k = 3
 * Output: [[1,3],[2,3]]
 * Explanation: All possible pairs are returned from the sequence: [1,3],[2,3]
 *
 * Constraints:
 * 1 <= nums1.length, nums2.length <= 105
 * -109 <= nums1[i], nums2[i] <= 109
 * nums1 and nums2 both are sorted in ascending order.
 * 1 <= k <= 104
 */
public class KPairWithSmallestSum {
    class Pair {
        Integer val1;
        Integer val2;
        public Pair(Integer val1, Integer val2){
            this.val1 = val1;
            this.val2 = val2;
        }

        @Override
        public int hashCode() {
            int hashcode =  this.val1 != null? val1.hashCode() : 0;
            return this.val2 != null? (hashcode * 31 + val2.hashCode()) : hashcode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != this.getClass()) return false;
            Pair that = (Pair) o;
            return this.val1.equals(that.val1) && this.val2.equals(that.val2);
        }
    }
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        int m = nums1.length;
        int n = nums2.length;

        Set<Pair> visited = new HashSet<>();

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{nums1[0] + nums2[0], 0, 0});
        visited.add(new Pair(0, 0));
        List<List<Integer>> result = new ArrayList<>();
        while(k-- > 0 && !pq.isEmpty()) {
            int[] val = pq.poll();
            int i = val[1], j = val[2];
            result.add(Arrays.asList(nums1[i], nums2[j]));

            if (i + 1 < m && !visited.contains(new Pair(i + 1, j))) {
                pq.offer(new int[]{ nums1[i + 1] + nums2[j], i + 1, j});
                visited.add(new Pair(i + 1, j));
            }

            if (j + 1 < n && !visited.contains(new Pair(i, j + 1))) {
                pq.offer(new int[]{ nums1[i] + nums2[j + 1], i, j + 1});
                visited.add(new Pair(i, j + 1));
            }
        }
        return result;
    }
}
