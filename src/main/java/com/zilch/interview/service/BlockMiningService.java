package com.zilch.interview.service;

import com.zilch.interview.model.pojo.MutableBlock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BlockMiningService {

    @Value("${blockchain.prefixLength}")
    private int prefixLength;

    /* This is simplistic method of mining for the project purpose. Prefix is used to add complexity, to meet required
       time between mined blocks. Usually the process is much more complex and miners constantly manipulate nonce value
       to find the correct hash. There is only one correct hash per block. */
    public String mineBlock(MutableBlock block) {
        while (!isPrefixComplexityMet(block.getHash())) {
            block.setNonce(block.getNonce() + 1); // just bruteforce
            block.setHash(BlockHashCalculator.calculate(block));
        }
        return block.getHash();
    }

    private boolean isPrefixComplexityMet(String hash) {
        String prefixString = "0".repeat(prefixLength);
        return hash.substring(0, prefixLength).equals(prefixString);
    }
}

