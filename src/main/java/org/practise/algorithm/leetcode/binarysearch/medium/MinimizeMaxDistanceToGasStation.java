package org.practise.algorithm.leetcode.binarysearch.medium;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * On a horizontal number line, we have gas stations at positions stations[0], stations[1], ..., stations[N-1], where N = stations.length.
 *
 * Now, we add K more gas stations so that D, the maximum distance between adjacent gas stations, is minimized.
 *
 * Return the smallest possible value of D.
 *
 * Example:
 *
 * Input: stations = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], K = 9
 * Output: 0.500000
 * Note:
 *
 * stations.length will be an integer in range [10, 2000].
 * stations[i] will be an integer in range [0, 10^8].
 * K will be an integer in range [1, 10^6].
 * Answers within 10^-6 of the true value will be accepted as correct.
 */
public class MinimizeMaxDistanceToGasStation {
    public double minmaxGasDist(int[] stations, int K) {
        double lo = 0, hi = 1e8;
        while (hi - lo > 1e-6) {
            double mi = (lo + hi) / 2.0;
            if (possible(mi, stations, K))
                hi = mi;
            else
                lo = mi;
        }
        return lo;
    }

    public boolean possible(double D, int[] stations, int K) {
        int used = 0;
        for (int i = 0; i < stations.length - 1; ++i)
            used += (int) ((stations[i+1] - stations[i]) / D);
        return used <= K;
    }

    // Approach #2: Brute Force [Time Limit Exceeded] - O(NK)
    public double minmaxGasDist2(int[] stations, int K) {
        int N = stations.length;
        double[] deltas = new double[N-1];
        for (int i = 0; i < N-1; ++i)
            deltas[i] = stations[i+1] - stations[i];

        int[] count = new int[N-1];
        Arrays.fill(count, 1);

        for (int k = 0; k < K; ++k) {
            // Find interval with largest part
            int best = 0;
            for (int i = 0; i < N-1; ++i)
                if (deltas[i] / count[i] > deltas[best] / count[best])
                    best = i;

            // Add gas station to best interval
            count[best]++;
        }

        double ans = 0;
        for (int i = 0; i < N-1; ++i)
            ans = Math.max(ans, deltas[i] / count[i]);

        return ans;
    }
    // Approach #3: Heap [Time Limit Exceeded] O(N+KlogN)
    public double minmaxGasDist3(int[] stations, int K) {
        int N = stations.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) ->
            (((double) b[0]) / b[1] < ((double) a[0])/ a[1]) ? -1: 1
        );

        for (int i = 0; i < N - 1; i++) {
            pq.offer(new int[] {stations[i + 1] - stations[i], 1});
        }

        for (int k = 0; k < K; k++) {
            int[] node = pq.poll();
            node[1]++;
            pq.offer(node);
        }
        int[] node = pq.poll();
        return node[0]/ ( (double) node[1]);
    }
}
