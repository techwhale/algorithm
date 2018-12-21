package org.practise.algorithm.interviewbit.divideandconquer;

/**
 *
 Inversion Count for an array indicates – how far (or close) the array is from being sorted.
 If array is already sorted then inversion count is 0. If array is sorted in reverse order that inversion count is the maximum.
 Formally speaking, two elements a[i] and a[j] form an inversion if a[i] > a[j] and i < j

 Example:
 The sequence 2, 4, 1, 3, 5 has three inversions (2, 1), (4, 1), (4, 3).
 */
public class CountInversion {
    public int countInversions(int[] arr) {
        int[] temp = new int[arr.length];
        return mergesort(arr, temp, 0, arr.length - 1);
    }

    private int mergesort(int arr[], int temp[], int left, int right) {
        int inversion_count = 0;
        if (left < right) {
            int mid = (left + right) / 2;
            inversion_count = mergesort(arr, temp, left, mid);
            inversion_count += mergesort(arr, temp, mid + 1, right);
            inversion_count += merge(arr, temp, left, mid  + 1, right);
        }
        return inversion_count;
    }


    private int merge(int arr[], int temp[], int left, int mid, int right) {
        int i = left, j = mid, k = left, inversion_count = 0;

        while (i <= mid - 1 && j <= right) {
            if (arr[i] > arr[j]) {
                inversion_count += (mid  - i);
                temp[k++] = arr[j++];
            } else {
                temp[k++] = arr[i++];
            }
        }

        while (i <= mid - 1) {
            temp[k++] = arr[i++];
        }

        while (j <= right) {
            temp[k++] = arr[j++];
        }

        for ( i = left; i <= right; i++) {
            arr[i] = temp[i];
        }

        return inversion_count;
    }
}
