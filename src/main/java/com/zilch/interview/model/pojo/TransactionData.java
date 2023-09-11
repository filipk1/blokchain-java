package com.zilch.interview.model.pojo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionData {
    String from;
    String to;
    int amount;

    public static TransactionData genesis() {
        return new TransactionData("0", "0", 0);
    }
}
