package com.zilch.interview;

import com.zilch.interview.aws.SqsService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
@Log
@Component
public class ApplicationReadyListener {
    private final SqsService sqsService;
    @Autowired
    public ApplicationReadyListener(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        log.info("Starting SQS consumer");
        sqsService.startSqsConsumer();
    }
}
