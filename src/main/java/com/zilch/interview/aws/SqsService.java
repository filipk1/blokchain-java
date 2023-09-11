package com.zilch.interview.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.zilch.interview.model.pojo.TransactionData;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Service
public class SqsService {
    private final AmazonSQS sqs;
    private final List<TransactionData> transactions;

    @Value("${sqs.url}")
    private String sqsUrl;

    @Value("${blockchain.transactionLimit}")
    private int transactionLimit;

    @Autowired
    public SqsService(AmazonSQS sqs) {
        this.sqs = sqs;
        this.transactions = new ArrayList<>(transactionLimit);
    }

    public void startSqsConsumer() {
        log.info("SQS consumer started");
        while (transactions.size() <= transactionLimit) {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl)
                    .withWaitTimeSeconds(5)
                    .withMaxNumberOfMessages(10);

            List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : sqsMessages) {
                Optional<TransactionData> transactionData = convertMessage(message.getBody());
                transactionData.ifPresent(transactions::add);
            }
        }
    }

    public boolean isReadyToMine() {
        return !transactions.isEmpty();
    }

    public List<TransactionData> getTransactions() {
        List<TransactionData> immutableCopy = List.copyOf(transactions);
        transactions.clear();
        return immutableCopy;
    }

    private Optional<TransactionData> convertMessage(String body) {
        log.warning("converting message" + body);
        String[] split = body.trim().split(",");
        if (!isValid(split)) {
            return Optional.empty();
        }

        return Optional.of(TransactionData.builder()
                .from(split[0].trim())
                .to(split[1].trim())
                .amount(Integer.parseInt(split[2].trim())).build());
    }

    private boolean isValid(String[] split) {
        return split.length == 3 && split[0] != null && split[1] != null && split[2] != null;
    }
}
