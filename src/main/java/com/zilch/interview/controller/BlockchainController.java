package com.zilch.interview.controller;

import com.zilch.interview.aws.SqsService;
import com.zilch.interview.model.pojo.BlockData;
import com.zilch.interview.model.pojo.ImmutableBlock;
import com.zilch.interview.model.pojo.MutableBlock;
import com.zilch.interview.model.Blockchain;
import com.zilch.interview.service.BlockMiningService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log
@RestController
@RequestMapping("/blockchain")
public class BlockchainController {
    private final Blockchain blockchain;
    private final BlockMiningService miningService;
    private final SqsService sqsService;

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
    public String mineBlock(@RequestBody BlockData data) {
        MutableBlock newBlock = new MutableBlock(data, blockchain.getLatestBlock().getHash(), System.currentTimeMillis());
        String minedHash = miningService.mineBlock(newBlock); // TODO: refactor this mining; addBlock should not mine but validate and add
        blockchain.addBlock(newBlock);

        return minedHash;
    }

    @PostMapping("/validate")
    public String validateTransaction(@RequestBody BlockData data) {
        // TODO: sanity check

        Optional<ImmutableBlock> result = blockchain.isTransactionValid(data);
        return result.isPresent() ? result.get().toString() : "NOT_FOUND";
    }

    @GetMapping("/sqs")
    public String getSqsMessage() {
        return sqsService.testSQS();
    }
}

