package com.microservices.app.twitter.to.kafka.service.runner.impl;

import com.microservices.app.config.data.TwitterToKafkaServiceConfigData;
import com.microservices.app.twitter.to.kafka.service.listener.TwitterKafkaListener;
import com.microservices.app.twitter.to.kafka.service.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-mock-tweets", havingValue = "false", matchIfMissing = true)
public class TwitterKafkaStreamRunner implements StreamRunner {

    private final TwitterToKafkaServiceConfigData configData;
    private final TwitterKafkaListener twitterKafkaListener;
    private TwitterStream twitterStream;

    @Override
    public void start() throws TwitterException{
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaListener);
        addFilter();
    }

    @PreDestroy
    public void shutdown(){
        if (twitterStream != null){
            log.info("Closing twitter stream!");
            twitterStream.shutdown();
        }
    }

    private void addFilter() {
        String[] keywords = configData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        log.info("Started filtering twitter stream for keywords {}", Arrays.toString(keywords));
    }
}
