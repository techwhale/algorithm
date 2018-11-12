package org.practise.algorithm.interviewbit.tree.medium;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class HotelReviewsTest {
    private HotelReviews reviews = new HotelReviews();

    /**
     *  * Input:
     *  * S = "cool_ice_wifi"
     *  * R = ["water_is_cool", "cold_ice_drink", "cool_wifi_speed"]
     *  *
     *  * Output:
     *  * ans = [2, 0, 1]
     *  * Here, sorted reviews are ["cool_wifi_speed", "water_is_cool", "cold_ice_drink"]
     */
    @Test
    public void testReviews() {
        final List<Integer> result = reviews.solve("cool_ice_wifi", Arrays.asList("water_is_cool", "cold_ice_drink", "cool_wifi_speed"));
        Assert.assertEquals(result, Arrays.asList(2, 0, 1));
    }
}