package org.practise.algorithm.interviewbit.binarysearch;

import java.util.List;

public class MedianOfTwoSortedArray {
    // DO NOT MODIFY BOTH THE LISTS
    public double findMedianSortedArrays(final List<Integer> a, final List<Integer> b) {

        if (a.size() > b.size()) {
            return findMedianSortedArrays(b, a);
        }

        int x = a.size(), y = b.size(), low = 0, high = x;
        while (low <= high) {
            int partitionX = (low + high)/ 2;
            int partitionY = (x + y + 1)/2 - partitionX;

            int leftX = partitionX == 0 ? Integer.MIN_VALUE: a.get(partitionX - 1);
            int leftY = partitionY == 0 ? Integer.MIN_VALUE : b.get(partitionY - 1);

            int rightX = partitionX >= x ? Integer.MAX_VALUE: a.get(partitionX);
            int rightY = partitionY >= y ? Integer.MAX_VALUE: b.get(partitionY);

            if (leftX <= rightY && rightX >= leftY) {
                double median;
                if ( (x + y) % 2 == 0 ) {
                    median =  ((double) Math.max(leftX, leftY) + (double) Math.min(rightX, rightY)) / 2;
                } else {
                    median = Math.max(leftX, leftY);
                }
                return median;
            } else if (leftX > rightY) {
                high = partitionX - 1;
            } else {
                low = partitionX + 1;
            }
        }
        throw new IllegalArgumentException();
    }
}
