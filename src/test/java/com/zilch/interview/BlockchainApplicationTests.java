package com.zilch.interview;

import com.zilch.interview.model.pojo.BlockData;
import com.zilch.interview.model.pojo.MutableBlock;
import com.zilch.interview.model.pojo.TransactionData;
import com.zilch.interview.service.BlockHashCalculator;
import com.zilch.interview.service.BlockMiningService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@TestPropertySource("application.properties")
public class BlockchainApplicationTests {
	private static final int PREFIX = 4;
	private List<MutableBlock> blockchain;
	private String prefixString;
	private BlockMiningService service;
	@Before
	public void setup(){
		blockchain = Lists.newArrayList();
		prefixString = new String(new char[PREFIX]).replace('\0', '0');
		service = new BlockMiningService();
		MutableBlock newBlock = new MutableBlock(
				BlockData.genesis(),
				"0",
				new Date().getTime());

		service.mineBlock(newBlock);
		blockchain.add(newBlock);
	}
	@Test
	public void givenBlockchain_whenNewBlockAdded_thenSuccess() {
		BlockData blockData = new BlockData();
		blockData.add(TransactionData.builder().from("xyz").to("pod").amount(170).build());
		MutableBlock newBlock = new MutableBlock(
				blockData,
				blockchain.get(blockchain.size() - 1).getHash(),
				new Date().getTime());
		service.mineBlock(newBlock);
        assertEquals(newBlock.getHash().substring(0, 4), prefixString);
		blockchain.add(newBlock);
		System.out.println("Blockchain: " + blockchain);
	}

	@Test
	public void givenBlockchain_whenValidated_thenSuccess() {
		boolean flag = true;
		for (int i = 0; i < blockchain.size(); i++) {
			String previousHash = i==0 ? "0" : blockchain.get(i - 1).getHash();
			flag = blockchain.get(i).getHash().equals(BlockHashCalculator.calculate(blockchain.get(i)))
					&& previousHash.equals(blockchain.get(i).getPreviousHash())
					&& blockchain.get(i).getHash().substring(0, PREFIX).equals(prefixString);
			if (!flag) break;
		}
		assertTrue(flag);
	}
}
