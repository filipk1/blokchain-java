package com.zilch.interview.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Service
public class SqsService {
    private static final String QUEUE_URL = "https://sqs.eu-central-1.amazonaws.com/103841698444/transactions.fifo";
    private final AmazonSQS sqs;
    @Autowired
    public SqsService(AmazonSQS sqs){
        this.sqs = sqs;
    }

    public String testSQS(){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(QUEUE_URL)
                .withWaitTimeSeconds(10)
                .withMaxNumberOfMessages(10);

        List<Message> sqsMessages = sqs.receiveMessage(receiveMessageRequest).getMessages();


        String body = sqsMessages.get(0).getBody();
        log.warning(body);

        return body;
    }
}
