package com.zilch.interview.controller;

import com.zilch.interview.model.MutableBlock;
import com.zilch.interview.model.Blockchain;
import com.zilch.interview.service.BlockMiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {
    private final Blockchain blockchain;
    private final BlockMiningService miningService;

    @Autowired
    public BlockchainController(Blockchain blockchain, BlockMiningService miningService) {
        this.blockchain = blockchain;
        this.miningService = miningService;
    }

    // Get the entire blockchain
    @GetMapping("/blocks")
    public String getBlockchain() {
        return blockchain.toString();
    }

    @PostMapping("/mine")
    public String mineBlock(@RequestBody String data) {
        MutableBlock newBlock = new MutableBlock(data, blockchain.getLatestBlock().getHash(), System.currentTimeMillis());
        String minedHash = miningService.mineBlock(newBlock); // TODO: refactor this mining; addBlock should not mine but validate and add
        blockchain.addBlock(newBlock);

        return minedHash;
    }
}

