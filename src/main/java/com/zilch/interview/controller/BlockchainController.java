package com.zilch.interview.controller;

import com.zilch.interview.aws.SqsService;
import com.zilch.interview.model.Blockchain;
import com.zilch.interview.model.pojo.BlockData;
import com.zilch.interview.model.pojo.ImmutableBlock;
import com.zilch.interview.model.pojo.MutableBlock;
import com.zilch.interview.model.pojo.TransactionData;
import com.zilch.interview.service.BlockMiningService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log
@RestController
@RequestMapping("/blockchain")
public class BlockchainController {
    private final Blockchain blockchain;
    private final BlockMiningService miningService;
    private final SqsService sqsService;
    private final Lock lock = new ReentrantLock();

    @Autowired
    public BlockchainController(Blockchain blockchain, BlockMiningService miningService, SqsService sqsService) {
        this.blockchain = blockchain;
        this.miningService = miningService;
        this.sqsService = sqsService;
    }

    // Get the entire blockchain
    @GetMapping("/blocks")
    public String getBlockchain() {
        return blockchain.toString();
    }

    @PostMapping("/mine")
    public String mineBlock() {
        lock.lock();
        try {
            if (!sqsService.isReadyToMine()) {
                return "NO_TRANSACTIONS_TO_MINE";
            }

            MutableBlock newBlock = prepareBlockToMine();
            String minedHash = miningService.mineBlock(newBlock);
            blockchain.addBlock(newBlock);

            return minedHash;
        } finally {
            lock.unlock();
        }
    }

    @PostMapping("/validate")
    public String validateTransaction(@RequestBody TransactionData transactionData) {
        // TODO: sanity check if local sql

        Optional<ImmutableBlock> result = blockchain.findTransaction(transactionData);
        return result.isPresent() ? result.get().toString() : "NOT_FOUND";
    }

    private MutableBlock prepareBlockToMine() {
        BlockData blockData = new BlockData();

        for (TransactionData trxData : sqsService.getTransactions()) {
            if (!blockData.add(trxData)) {
                log.warning("Transaction rejected because of limit: " + trxData.toString());
                break;
            }
        }

        return new MutableBlock(blockData, blockchain.getLatestBlock().getHash(), System.currentTimeMillis());
    }
}

