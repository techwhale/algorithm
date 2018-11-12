package org.practise.algorithm.interviewbit.backtracking;

import java.util.ArrayList;
import java.util.Arrays;

public class NQueens {
    private char[] columnAssignment;
    private char[][] diagonalAssignment;
    public ArrayList<ArrayList<String>> solveNQueens(int N) {
        ArrayList<ArrayList<String>> resultList = new ArrayList<>();
        if (N <= 0) return resultList;
        char[][] board = new char[N][N];
        columnAssignment = new char[N];
        diagonalAssignment = new char[N * 2 - 1][2];
        fillEmptyArray(columnAssignment, diagonalAssignment, board);
        dfs(board, N, 0, N, resultList);
        return resultList;
    }

    private void dfs(char[][] board, int nQueen, int row, int N, ArrayList<ArrayList<String>> resultList) {
        if (nQueen == 0) {
            ArrayList<String> possibleOption = convertBoardToList(board);
            resultList.add(possibleOption);
            return;
        }
        if (row >= N) return;
        for (int col = 0; col < N; col++) {
            if (isQueenAssignable(row, col, N)) {
                assignQueen(row, col, N, board);
                dfs(board, nQueen - 1, row + 1, N, resultList);
                unAssignQueen(row, col, N, board);
            }
        }
    }

    private ArrayList<String> convertBoardToList(char[][] board) {
        ArrayList<String> list = new ArrayList<>();
        for (char[] row : board) {
            list.add(new String(row));
        }
        return list;
    }

    private boolean isQueenAssignable(int row, int col, int N) {
        boolean isQueenPresent = (columnAssignment[col] == 'Q'
                || diagonalAssignment[row + col][0] == 'Q' || diagonalAssignment[(N - row - 1) + col][1] == 'Q');
        return !isQueenPresent;
    }

    private void assignQueen(int row, int col, int N, char[][] board) {
        columnAssignment[col] = 'Q';
        diagonalAssignment[row + col][0] = 'Q';
        diagonalAssignment[(N - row - 1) + col][1] = 'Q';
        board[row][col] = 'Q';
    }

    private void unAssignQueen(int row, int col, int N, char[][] board) {
        columnAssignment[col] = '.';
        diagonalAssignment[row + col][0] = '.';
        diagonalAssignment[(N - row - 1) + col][1] = '.';
        board[row][col] = '.';
    }

    private void fillEmptyArray(char[] arr1, char[][] arr2, char[][] board) {
        Arrays.fill(arr1, '.');
        for (char[] arr: arr2) {
            Arrays.fill(arr, '.');
        }
        for (char[] arr: board) {
            Arrays.fill(arr, '.');
        }
    }
}
