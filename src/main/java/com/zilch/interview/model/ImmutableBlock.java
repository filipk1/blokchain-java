package com.zilch.interview.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString // TODO: write custom toString to visualize it
public class ImmutableBlock implements Block{

    private final @NonNull String hash;
    private final @NonNull String previousHash;
    private final @NonNull String data;
    private final long timeStamp;
    private final int nonce;
}
