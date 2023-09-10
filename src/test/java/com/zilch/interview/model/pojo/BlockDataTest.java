package com.zilch.interview.model.pojo;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BlockDataTest {

    @Test
    public void testCapacityShouldNotAddMoreThan10Elements() {
        BlockData blockData = new BlockData();

        for (int i = 0; i < 11; i++) {
            boolean isAdded = blockData.add(TransactionData.builder()
                    .from("ax" + i)
                    .to("az" + i)
                    .amount(i)
                    .build());

            if (i < 10) {
                assertTrue(isAdded);
            } else {
                assertFalse(isAdded);
            }
        }
    }
}