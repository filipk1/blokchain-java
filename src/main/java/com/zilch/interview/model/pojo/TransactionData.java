package com.zilch.interview.model.pojo;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionData {
    private final String from;
    private final String to;
    private final int amount;

    public static TransactionData genesis(){
        return new TransactionData("0", "0", 0);
    }
}
