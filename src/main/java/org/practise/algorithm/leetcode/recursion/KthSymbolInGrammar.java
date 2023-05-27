package org.practise.algorithm.leetcode.recursion;

/**
 * 779. K-th Symbol in Grammar
 *
 * On the first row, we write a 0. Now in every subsequent row, we look at the previous row and replace each occurrence of 0 with 01,
 * and each occurrence of 1 with 10.
 *
 * Given row N and index K, return the K-th indexed symbol in row N. (The values of K are 1-indexed.) (1 indexed).
 *
 * Examples:
 * Input: N = 1, K = 1
 * Output: 0
 *
 * Input: N = 2, K = 1
 * Output: 0
 *
 * Input: N = 2, K = 2
 * Output: 1
 *
 * Input: N = 4, K = 5
 * Output: 1
 *
 * Explanation:
 * row 1: 0
 * row 2: 01
 * row 3: 0110
 * row 4: 01101001
 * Note:
 *
 * N will be an integer in the range [1, 30].
 * K will be an integer in the range [1, 2^(N-1)]
 */
public class KthSymbolInGrammar {
    public int kthGrammar(int N, int K) {
        // Implemented based on following explanation
        // https://leetcode.com/problems/k-th-symbol-in-grammar/solutions/1151025/java-easy-to-understand-solution/
        //Row1	        			  0
        //						  /         \
        //Row2					0            1
        //					   /   \        /    \
        //Row3				  0     1       1     0
        //					 / \    / \    / \    / \
        //Row4				0  1   1   0  1  0   0   1
        //
        //Index(for Row 4)  1  2   3   4  5  6  7  8

        if (N == 1) {
            return 0;
        }
        int result;
        if (K % 2 == 1) {
            result = kthGrammar(N -1, (K + 1)/ 2);
        } else {
            result = kthGrammar(N -1, K/ 2) ^ 1;
        }
        return result;
    }
}
