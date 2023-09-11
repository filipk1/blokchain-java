package com.zilch.interview.model;

import com.zilch.interview.model.pojo.BlockData;
import com.zilch.interview.model.pojo.MutableBlock;
import com.zilch.interview.model.pojo.TransactionData;
import com.zilch.interview.service.BlockMiningService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Blockchain.class, BlockMiningService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BlockchainTest {

    @Autowired
    private Blockchain blockchain;

    @Autowired
    private BlockMiningService blockMiningService;

    @Test
    public void shouldCreateGenesisBlockOnInitialization() {
        assertEquals(blockchain.getLatestBlock().getPreviousHash(), "0");
        assertTrue(blockchain.getLatestBlock().getData().contains(TransactionData.genesis()));
    }

    @Test
    public void shouldAddValidBlock() {
        String previousHash = blockchain.getLatestBlock().getHash();
        MutableBlock block = createBlock(previousHash, "xyz", "abc", 100);

        String minedHash = blockMiningService.mineBlock(block);
        blockchain.addBlock(block);

        assertFalse(blockchain.getLatestBlock().getData().contains(TransactionData.genesis()));
        assertEquals(previousHash, blockchain.getLatestBlock().getPreviousHash());
        assertEquals(minedHash, blockchain.getLatestBlock().getHash());
    }

    @Test
    public void shouldRejectInvalidBlock(){
        String previousHash = blockchain.getLatestBlock().getHash();
        MutableBlock block = createBlock(previousHash, "xyz", "abc", 100);

        String minedHash = blockMiningService.mineBlock(block);
        block.setHash("00465735832dfshfja98sd6");
        blockchain.addBlock(block);

        assertEquals(previousHash, blockchain.getLatestBlock().getHash());
        assertNotEquals(minedHash, blockchain.getLatestBlock().getHash());
    }

    @Test
    public void shouldFindTransaction(){
        String from = "aaa";
        String to = "bbb";
        int amount = 1000;
        MutableBlock block = createBlock(blockchain.getLatestBlock().getHash(), from, to, amount);

        blockMiningService.mineBlock(block);
        blockchain.addBlock(block);

        TransactionData trxData = TransactionData.builder()
                .from(from)
                .to(to)
                .amount(amount).build();

        assertTrue(blockchain.findTransaction(trxData).isPresent());
    }

    private MutableBlock createBlock(String previousHash, String from, String to, int amount) {
        TransactionData trxData = TransactionData.builder()
                .from(from)
                .to(to)
                .amount(amount).build();

        BlockData blockData = new BlockData();
        blockData.add(trxData);

        return new MutableBlock(blockData, previousHash, System.currentTimeMillis());
    }

}