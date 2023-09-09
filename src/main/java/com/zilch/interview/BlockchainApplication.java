package com.zilch.interview;

import com.zilch.interview.model.Blockchain;
import com.zilch.interview.service.BlockMiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainApplication.class, args);
	}

	@Bean
	public Blockchain blockchain(@Autowired BlockMiningService miningService) {
		return new Blockchain(miningService);
	}
}
