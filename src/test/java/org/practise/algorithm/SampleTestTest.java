package org.practise.algorithm;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SampleTestTest {
    SampleTest sampleTest = new SampleTest();

    @Test
    public void testTopKFrequent() {
        int[] val = {1,1,1,2,2,3};

        sampleTest.topKFrequent(val, 2);
    }
}