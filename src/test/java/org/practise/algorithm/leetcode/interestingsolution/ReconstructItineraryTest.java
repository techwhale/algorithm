package org.practise.algorithm.leetcode.interestingsolution;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class ReconstructItineraryTest {
    ReconstructItinerary obj = new ReconstructItinerary();
    @Test
    public void testFindItinerary() {
        List<List<String>> tickets = new ArrayList<>();
        tickets.add(Arrays.asList("JFK", "AAA"));
        tickets.add(Arrays.asList("CCC", "JFK"));
        tickets.add(Arrays.asList("BBB", "CCC"));
        tickets.add(Arrays.asList("JFK", "BBB"));
        obj.findItinerary(tickets);
    }
}