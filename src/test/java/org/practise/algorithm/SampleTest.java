package org.practise.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class SampleTest {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>((a , b) -> map.get(a) - map.get(b));
        for (Integer key : map.keySet()) {
            pq.offer(key);
            while (pq.size() > k) {
                pq.poll();
            }
        }
        int[] result = new int[pq.size()];
        int idx = pq.size() - 1;
        while(pq.isEmpty()) {
            result[idx--] = pq.poll();
        }
        return result;
    }
}
