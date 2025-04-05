package com.wikipedia.producer.kafkaproducer;

import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import com.wikipedia.producer.config.WikimediaChangesHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class WikimediaChangesProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private BackgroundEventSource eventSource;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

//    public void sendMessage() {
//        String topic = "wikimedia_recentchange";
//        BackgroundEventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate ,topic);
//        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
//        this.eventSource = new BackgroundEventSource.Builder(eventHandler,
//                new EventSource.Builder(ConnectStrategy.http(URI.create(url)))).build();
//        this.eventSource.start();
//    }

    @PostConstruct
    public void sendMessage() {
        String topic = "wikimedia_recentchange";
        BackgroundEventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate ,topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        this.eventSource = new BackgroundEventSource.Builder(eventHandler,
                new EventSource.Builder(ConnectStrategy.http(URI.create(url)))).build();

        executorService.submit(() -> {
            log.info("Starting wikimedia..");
            this.eventSource.start();
        });
    }

    @PreDestroy
    public void preDestroy() {
        if (this.eventSource!= null) {
            eventSource.close();
        }
        log.info("Shutting down Wikimedia...");
        executorService.shutdown();
    }
}
