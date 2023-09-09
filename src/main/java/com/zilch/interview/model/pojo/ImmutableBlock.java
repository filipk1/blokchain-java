package com.zilch.interview.model.pojo;

import com.zilch.interview.model.Block;
import com.zilch.interview.model.pojo.BlockData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString // TODO: write custom toString to visualize it better
public class ImmutableBlock implements Block {

    private final @NonNull String hash;
    private final @NonNull String previousHash;
    private final @NonNull BlockData data;
    private final long timeStamp;
    private final int nonce;
}
