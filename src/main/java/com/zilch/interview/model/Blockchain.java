package com.zilch.interview.model;

import com.zilch.interview.BlockHashCalculator;
import com.zilch.interview.model.pojo.BlockData;
import com.zilch.interview.model.pojo.ImmutableBlock;
import com.zilch.interview.model.pojo.MutableBlock;
import com.zilch.interview.service.BlockMiningService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zilch.interview.service.BlockMiningService.DEFAULT_PREFIX_LENGTH;

public class Blockchain {
    private final List<ImmutableBlock> chain;
    private final BlockMiningService miningService;

    @Autowired
    public Blockchain(BlockMiningService miningService) {
        this.chain = new ArrayList<>();
        this.miningService = miningService;
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        MutableBlock genesisBlock = new MutableBlock(BlockData.genesis(), "0", System.currentTimeMillis());
        miningService.mineBlock(genesisBlock);
        chain.add(genesisBlock.toImmutable());
    }

    public ImmutableBlock getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    //TODO: synchronized?
    public void addBlock(MutableBlock block) {
        block.setPreviousHash(getLatestBlock().getHash());
        if (isBlockValid(block)) {
            chain.add(block.toImmutable());
        }
    }

    public Optional<ImmutableBlock> isTransactionValid(BlockData blockData) {
        return chain.stream()
                .filter(block -> block.getData().equals(blockData))
                .findAny();

        //return chain.stream().anyMatch(block -> block.getData().equals(blockData));
    }

    public boolean isBlockValid(Block block) {
        if (chain.isEmpty()) return true;

        return block.getHash().equals(BlockHashCalculator.calculate(block))
                && getLatestBlock().getHash().equals(block.getPreviousHash())
                && block.getHash().substring(0, DEFAULT_PREFIX_LENGTH).equals(new String(new char[DEFAULT_PREFIX_LENGTH]).replace('\0', '0'));
    }

//    public boolean isValid(){ // this validates whole chain
//        boolean isValid = true;
//        for (int i = 0; i < chain.size(); i++) {
//            String previousHash = i==0 ? "0" : chain.get(i - 1).getHash();
//            isValid = chain.get(i).getHash().equals(BlockHashCalculator.calculate(chain.get(i)))
//                    && previousHash.equals(chain.get(i).getPreviousHash())
//                    && chain.get(i).getHash().substring(0, DEFAULT_PREFIX_LENGTH).equals(new String(new char[DEFAULT_PREFIX_LENGTH]).replace('\0', '0'));
//            if (!isValid) break;
//        }
//
//        return isValid;
//    }

    public String toString() {
        return chain.toString();
    }
}

