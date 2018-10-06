package org.practise.algorithm.interviewbit.binarysearch;

/*
You have to paint N boards of length {A0, A1, A2, A3 … AN-1}.
There are K painters available and you are also given how much time a painter takes to paint 1 unit of board.
You have to get this job done as soon as possible under the constraints that any painter will only paint contiguous sections of board.

2 painters cannot share a board to paint. That is to say, a board
cannot be painted partially by one painter, and partially by another.
A painter will only paint contiguous boards. Which means a
configuration where painter 1 paints board 1 and 3 but not 2 is
invalid.
Return the ans % 10000003

Input :
K : Number of painters
T : Time taken by painter to paint 1 unit of board
L : A List which will represent length of each board

Output:
     return minimum time to paint all boards % 10000003
Example

Input :
  K : 2
  T : 5
  L : [1, 10]
Output : 50

 */
public class PainterPartitionProblem {
    private static final int MODULO = 10000003;
    public int paint(int availablePainters, int timeToPaint, int[] boards) {
        int maxBoardTime = findSumOfAllBoardTime(boards);
        int minBoardTime = findMaxTimeForSingleBoard(boards);
        while (minBoardTime < maxBoardTime) {
            int midBoardTime = (int) (minBoardTime + (maxBoardTime - minBoardTime)/2) % MODULO;
            if (isFinishable(boards, midBoardTime, availablePainters)) { // check task is finishable with given painters
                maxBoardTime = midBoardTime;
            } else {
                minBoardTime = midBoardTime + 1;
            }
        }

        long minimumTimeToPaint = (long) minBoardTime * timeToPaint;
        return (int) (minimumTimeToPaint % MODULO);
    }

    private boolean isFinishable(int[] boards, int maxMergableBoard, int availablePainters) {
        int painters = 1;
        int currMergedBoard = 0;
        for (int board : boards) {
            currMergedBoard += board;

            if (currMergedBoard > maxMergableBoard) {
                painters++;
                currMergedBoard = board;

                if (availablePainters < painters) {
                    return false;
                }
            }
        }
        return true;
    }

    private int findMaxTimeForSingleBoard(int[] boards) {
        int max = 0;
        for (int board : boards) {
            max = Math.max(max, board);
        }
        return max;
    }

    private int findSumOfAllBoardTime(int[] boards) {
        int sum = 0;
        for (int board : boards) {
            sum += board;
        }
        return sum;
    }
}
