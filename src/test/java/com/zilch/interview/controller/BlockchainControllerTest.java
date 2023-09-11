package com.zilch.interview.controller;

import com.zilch.interview.aws.SqsService;
import com.zilch.interview.model.Blockchain;
import com.zilch.interview.model.pojo.ImmutableBlock;
import com.zilch.interview.model.pojo.TransactionData;
import com.zilch.interview.service.BlockMiningService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class BlockchainControllerTest {
    @InjectMocks
    BlockchainController controller;

    @Mock
    Blockchain blockchain;
    @Mock
    BlockMiningService miningService;
    @Mock
    SqsService sqsService;

    @Test
    public void testGetBlockchain(){
        String chainToString = "xxxyyyzzz";
        when(blockchain.toString()).thenReturn(chainToString);

        assertEquals(chainToString,controller.getBlockchain());
    }

    @Test
    public void testMineShouldReturnNoTransactionsToMine(){
        when(sqsService.isReadyToMine()).thenReturn(false);
        String response = controller.mineBlock();

        assertEquals("NO_TRANSACTIONS_TO_MINE", response);
    }

    @Test
    public void testMineShouldReturnMinedHash(){
        String minedHash = "0000xwiuyewf743grhodsffdsaf";
        ImmutableBlock block = mock(ImmutableBlock.class);

        when(sqsService.isReadyToMine()).thenReturn(true);
        when(sqsService.getTransactions()).thenReturn(List.of(createSampleTransaction(100), createSampleTransaction(59)));
        when(miningService.mineBlock(any())).thenReturn(minedHash);
        when(blockchain.getLatestBlock()).thenReturn(block);
        doNothing().when(blockchain).addBlock(any());

        String response = controller.mineBlock();
        assertEquals(minedHash, response);
    }

    @Test
    public void testValidateShouldReturnNotFound(){
        when(blockchain.findTransaction(any())).thenReturn(Optional.empty());
        String response = controller.validateTransaction(createSampleTransaction(100));

        assertEquals("NOT_FOUND", response);
    }

    @Test
    public void testValidateShouldReturnFoundBlock(){
        ImmutableBlock block = mock(ImmutableBlock.class);

        when(block.toString()).thenReturn("block #7");
        when(blockchain.findTransaction(any())).thenReturn(Optional.of(block));
        String response = controller.validateTransaction(createSampleTransaction(100));

        assertEquals("block #7", response);
    }

    private TransactionData createSampleTransaction(int amount){
        return TransactionData.builder()
                .from("aaa")
                .to("bbb")
                .amount(amount).build();
    }
}