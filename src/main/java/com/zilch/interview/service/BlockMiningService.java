package com.zilch.interview.service;

import com.zilch.interview.BlockHashCalculator;
import com.zilch.interview.model.MutableBlock;
import org.springframework.stereotype.Service;

@Service
public class BlockMiningService {
    public static final int DEFAULT_PREFIX_LENGTH = 4; // TODO: move that into the configuration

    /* This is maximum simplistic method of mining for the project purpose. Prefix is used to add complexity, to meet required
       time between mined blocks. Usually the process is much more complex and miners constantly manipulate nonce value
       trying to find the correct hash. There is only one correct hash per block. */
    public String mineBlock(MutableBlock block) {
        String prefixString = "0".repeat(DEFAULT_PREFIX_LENGTH);
        while (!block.getHash().substring(0, DEFAULT_PREFIX_LENGTH).equals(prefixString)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(BlockHashCalculator.calculate(block));
        }
        return block.getHash();
    }

    // TODO: isValid
}

