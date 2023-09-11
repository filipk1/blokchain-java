package com.zilch.interview.model.pojo;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class BlockData {
    private final List<TransactionData> transactions = new ArrayList<>(10);

    public boolean add(TransactionData transactionData) {
        if (transactions.size() >= 10) {
            return false;
        }
        return transactions.add(transactionData);
    }

    public boolean contains(TransactionData transactionData) {
        return transactions.contains(transactionData);
    }

    public static BlockData genesis() {
        BlockData blockData = new BlockData();
        blockData.add(TransactionData.genesis());

        return blockData;
    }
}
