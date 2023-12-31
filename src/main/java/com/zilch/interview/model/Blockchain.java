package com.zilch.interview.model;

import com.zilch.interview.model.pojo.BlockData;
import com.zilch.interview.model.pojo.ImmutableBlock;
import com.zilch.interview.model.pojo.MutableBlock;
import com.zilch.interview.model.pojo.TransactionData;
import com.zilch.interview.service.BlockHashCalculator;
import com.zilch.interview.service.BlockMiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Blockchain {
    private List<ImmutableBlock> chain;
    private final BlockMiningService miningService;

    @Value("${blockchain.prefixLength}")
    private int prefixLength;

    @Autowired
    public Blockchain(BlockMiningService miningService) {
        this.miningService = miningService;
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        MutableBlock genesisBlock = new MutableBlock(BlockData.genesis(), "0", System.currentTimeMillis());
        miningService.mineBlock(genesisBlock);
        chain = List.of(genesisBlock.toImmutable());
    }

    public ImmutableBlock getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    // synchronised on controller level
    public void addBlock(MutableBlock block) {
        block.setPreviousHash(getLatestBlock().getHash());
        if (isBlockValid(block)) {
            chain = Stream.concat(chain.stream(), Stream.of(block.toImmutable())).collect(Collectors.toUnmodifiableList());
        }
    }

    public Optional<ImmutableBlock> findTransaction(TransactionData transactionData) {
        return chain.stream()
                .filter(block -> block.getData().contains(transactionData))
                .findAny();
    }

    public boolean isBlockValid(Block block) {
        if (chain.isEmpty()) return true;

        return block.getHash().equals(BlockHashCalculator.calculate(block))
                && getLatestBlock().getHash().equals(block.getPreviousHash())
                && block.getHash().substring(0, prefixLength).equals(new String(new char[prefixLength]).replace('\0', '0'));
    }

    public String toString() {
        return chain.toString();
    }
}

