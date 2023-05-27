package org.practise.algorithm.leetcode.segmenttree;

/**
 * 2407. Longest Increasing Subsequence II
 * Hard
 * You are given an integer array nums and an integer k.
 * Find the longest subsequence of nums that meets the following requirements:
 * The subsequence is strictly increasing and
 * The difference between adjacent elements in the subsequence is at most k.
 * Return the length of the longest subsequence that meets the requirements.
 *
 * A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
 *
 * Example 1:
 * Input: nums = [4,2,1,4,3,4,5,8,15], k = 3
 * Output: 5
 * Explanation:
 * The longest subsequence that meets the requirements is [1,3,4,5,8].
 * The subsequence has a length of 5, so we return 5.
 * Note that the subsequence [1,3,4,5,8,15] does not meet the requirements because 15 - 8 = 7 is larger than 3.
 *
 * Example 2:
 * Input: nums = [7,4,5,1,8,12,4,7], k = 5
 * Output: 4
 * Explanation:
 * The longest subsequence that meets the requirements is [4,5,8,12].
 * The subsequence has a length of 4, so we return 4.
 *
 * Example 3:
 * Input: nums = [1,5], k = 1
 * Output: 1
 * Explanation:
 * The longest subsequence that meets the requirements is [1].
 * The subsequence has a length of 1, so we return 1.
 *
 * Constraints:
 * 1 <= nums.length <= 105
 * 1 <= nums[i], k <= 105
 */
public class LongestIncreasingSubsequenceII {
    public int lengthOfLIS(int[] nums, int k) {
        SegmentTree root = new SegmentTree(1, 100000);
        int res = 0;
        for (int num : nums) {
            int preMax = root.rangeMaxQuery(root, num - k, num - 1);
            root.update(root, num, preMax + 1);
            res = Math.max(res, preMax + 1);
        }
        return res;
    }

    class SegmentTree {
        SegmentTree left, right;
        int start, end, val;
        public SegmentTree(int start, int end) {
            this.start = start;
            this.end = end;
            setup(this, start, end);
        }
        public void setup(SegmentTree node, int start, int end) {
            if (start == end) return;
            int mid = start + (end - start) / 2;
            if (node.left == null) {
                node.left = new SegmentTree(start, mid);
                node.right = new SegmentTree(mid + 1, end);
            }
            setup(node.left, start, mid);
            setup(node.right, mid + 1, end);
            node.val = Math.max(node.left.val, node.right.val);
        }

        public void update(SegmentTree node, int index, int val) {
            if (index < node.start || index > node.end) return;
            if (node.start == node.end && node.start == index) {
                node.val = val;
                return;
            }
            update(node.left, index, val);
            update(node.right, index, val);
            node.val = Math.max(node.left.val, node.right.val);
        }

        public int rangeMaxQuery(SegmentTree node, int start, int end) {
            if (node.start > end || node.end < start) return 0;
            if (node.start >= start && node.end <= end) return node.val;
            return Math.max(rangeMaxQuery(node.left, start, end), rangeMaxQuery(node.right, start, end));
        }
    }
}
