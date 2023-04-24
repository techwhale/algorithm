package org.practise.algorithm.leetcode.design.medium;

import java.util.LinkedList;

/**
 * 1756. Design Most Recently Used Queue
 * Medium
 * Design a queue-like data structure that moves the most recently used element to the end of the queue.
 *
 * Implement the MRUQueue class:
 * MRUQueue(int n) constructs the MRUQueue with n elements: [1,2,3,...,n].
 * int fetch(int k) moves the kth element (1-indexed) to the end of the queue and returns it.
 *
 * Example 1:
 * Input:
 * ["MRUQueue", "fetch", "fetch", "fetch", "fetch"]
 * [[8], [3], [5], [2], [8]]
 * Output:
 * [null, 3, 6, 2, 2]
 *
 * Explanation:
 * MRUQueue mRUQueue = new MRUQueue(8); // Initializes the queue to [1,2,3,4,5,6,7,8].
 * mRUQueue.fetch(3); // Moves the 3rd element (3) to the end of the queue to become [1,2,4,5,6,7,8,3] and returns it.
 * mRUQueue.fetch(5); // Moves the 5th element (6) to the end of the queue to become [1,2,4,5,7,8,3,6] and returns it.
 * mRUQueue.fetch(2); // Moves the 2nd element (2) to the end of the queue to become [1,4,5,7,8,3,6,2] and returns it.
 * mRUQueue.fetch(8); // The 8th element (2) is already at the end of the queue so just return it.
 *
 *
 * Constraints:
 * 1 <= n <= 2000
 * 1 <= k <= n
 * At most 2000 calls will be made to fetch.
 *
 * Follow up: Finding an O(n) algorithm per fetch is a bit easy. Can you find an algorithm with a better complexity for each fetch call?
 */
public class MRUQueue {
    LinkedList[] buckets;
    int step;
    int size;
    public MRUQueue(int n) {
        step = (int)Math.sqrt(n); //the size of each bucket
        size = n / step + 1; // + 1 is for taking all numbers
        buckets = new LinkedList[size];
        for(int i = 0; i < size; i++) buckets[i] = new LinkedList<Integer>();
        for(int i = 1; i <= n; i++){
            buckets[i / step].add(i); //(i / step) determines which bucket we should the number put in
        }
    }

    public int fetch(int k) { //it takes O(2 * sqrt(N)) = O(sqrt(N))
        int m = k / step; //find the bucket that will contain our target number
        int index = m == 0 ? k % step - 1 : k % step; //index inside buckets[m]
        int ret = (int)buckets[m].remove(index); //it takes O(sqrt(N))
        buckets[size - 1].add(ret); //append it to the tail
        for(int i = m; i < size - 1; i++){ //rebalance the queue, it takes O(sqrt(N)) in total
            buckets[i].add(buckets[i + 1].pollFirst());
        }
        return ret;
    }
}
