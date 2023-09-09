package com.zilch.interview.model.pojo;

import com.zilch.interview.BlockHashCalculator;
import com.zilch.interview.model.Block;
import com.zilch.interview.model.pojo.BlockData;
import com.zilch.interview.model.pojo.ImmutableBlock;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MutableBlock implements Block {
    private @NonNull String hash;
    private @NonNull String previousHash;
    private @NonNull BlockData data; // in real-life scenario, this is limited by size (1MB for BTC, hard fork BCH to 32M to increase tps - 2017)
    private long timeStamp;
    private int nonce;

    public MutableBlock(BlockData data, String previousHash, long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = BlockHashCalculator.calculate(this);
    }

    public ImmutableBlock toImmutable() {
        return new ImmutableBlock(hash, previousHash, data, timeStamp, nonce);
    }
}
