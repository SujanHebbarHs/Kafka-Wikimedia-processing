package com.wikipedia.producer.config;

import com.wikipedia.producer.kafkaproducer.WikimediaChangesProducer;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This is a method that is used to call sendMessage from kafka producer during application start up.
 * Basically implementing CommandLineRunner allows us to perform some task just once as soon as thr application starts
 * This is done with the help of CommandLineRunner
 */
//@Component // Commenting this as using @PostConstruct in WikimediaChangesProducer to trigger what we are doing now with producer.sendMessage
@AllArgsConstructor
public class MethodToCallSendMessage implements CommandLineRunner {
    private WikimediaChangesProducer producer;

    @Override
    public void run(String... args) throws Exception {
//        producer.sendMessage();
    }
}
