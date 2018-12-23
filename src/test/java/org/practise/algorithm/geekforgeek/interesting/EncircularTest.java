package org.practise.algorithm.geekforgeek.interesting;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EncircularTest {
    private Encircular obj = new Encircular();

    @Test
    public void testEncircular() {
        Assert.assertTrue(obj.isCircle("RGRGRGRG"));
    }
}