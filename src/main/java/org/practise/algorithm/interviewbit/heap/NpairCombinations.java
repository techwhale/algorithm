package org.practise.algorithm.interviewbit.heap;

import java.util.*;

/**
 *Given two arrays A & B of size N each.
 * Find the maximum n elements from the sum combinations (Ai + Bj) formed from elements in array A and B.
 *
 * For example if A = [1,2], B = [3,4], then possible pair sums can be 1+3 = 4 , 1+4=5 , 2+3=5 , 2+4=6
 * and maximum 2 elements are 6, 5
 *
 * Example:
 *
 * N = 4
 * a[]={1,4,2,3}
 * b[]={2,5,1,6}
 *
 * Maximum 4 elements of combinations sum are
 * 10   (4+6),
 * 9    (3+6),
 * 9    (4+5),
 * 8    (2+6)
 */
public class NpairCombinations {
    class Triplet
    {
        int val, idxA, idxB;
        Triplet(int val, int idxA, int idxB)
        {
            this.val = val;
            this.idxA = idxA;
            this.idxB = idxB;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + idxA;
            result = prime * result + idxB;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Triplet other = (Triplet) obj;
            if (idxA != other.idxA)
                return false;
            if (idxB != other.idxB)
                return false;
            return true;
        }
    }
    public List<Integer> solve(List<Integer> A, List<Integer> B) {

        PriorityQueue<Triplet> pq = new PriorityQueue<>((x, y) -> (y.val - x.val));
        HashSet<Triplet> visited = new HashSet<>();
        ArrayList<Integer> ans = new ArrayList<>();
        Collections.sort(A, (x, y) -> (y-x));
        Collections.sort(B, (x,y) -> (y-x));

        int N = A.size();
        if(N==0) return ans;

        pq.offer(new Triplet(A.get(0)+B.get(0),0,0));

        while(!pq.isEmpty())
        {
            Triplet top = pq.remove();
            ans.add(top.val);
            if(top.idxA+1 < N)
            {
                Triplet next = new Triplet(A.get(top.idxA+1)+B.get(top.idxB), top.idxA+1, top.idxB);
                if(!visited.contains(next))
                {
                    pq.offer(next);
                    visited.add(next);
                }
            }
            if(top.idxB+1 < N)
            {
                Triplet next = new Triplet(A.get(top.idxA)+B.get(top.idxB+1), top.idxA, top.idxB+1);
                if(!visited.contains(next))
                {
                    pq.offer(next);
                    visited.add(next);
                }
            }
            if(ans.size()==N) break;
        }

        return ans;
    }
}
