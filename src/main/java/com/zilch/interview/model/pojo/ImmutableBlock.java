package com.zilch.interview.model.pojo;

import com.zilch.interview.model.Block;
import lombok.NonNull;
import lombok.Value;

@Value
public class ImmutableBlock implements Block {
    @NonNull String hash;
    @NonNull String previousHash;
    @NonNull BlockData data;
    long timeStamp;
    int nonce;
}
