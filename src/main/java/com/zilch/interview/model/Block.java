package com.zilch.interview.model;

import com.zilch.interview.BlockHashCalculator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Block {
    private String hash;
    private String previousHash;
    private String data; // in real-life scenario, this is limited by size (1MB for BTC, hard fork BCH to 32M to increase tps - 2017)
    private long timeStamp;
    private int nonce;

    public Block(String data, String previousHash, long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = BlockHashCalculator.calculate(this);
    }
}
