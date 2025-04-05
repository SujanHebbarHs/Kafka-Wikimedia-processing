package com.wikipedia.consumer.kafkaconsumer;

import com.wikipedia.consumer.entity.WikimediaData;
import com.wikipedia.consumer.repository.WikimediaDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaDatabaseConsumer {

    private final WikimediaDataRepository dataRepository;

    @KafkaListener(topics = "wikimedia_recentchange", groupId = "myGroup")
    public void consume(String msg) {
        log.info("Event message received: {}", msg);
        WikimediaData data = new WikimediaData();
        data.setWikiEventData(msg);
        dataRepository.save(data);

    }
}
