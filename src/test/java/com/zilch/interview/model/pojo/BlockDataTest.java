package com.zilch.interview.model.pojo;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BlockDataTest {

    @Test
    public void testCapacityShouldNotAddMoreThan10Elements() {
        BlockData blockData = new BlockData();

        IntStream.rangeClosed(0,11).forEach(i -> {
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
        });
    }
}