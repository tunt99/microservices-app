package com.microservices.app.twitter.to.kafka.service.runner.impl;

import com.microservices.app.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import com.microservices.app.twitter.to.kafka.service.listener.TwitterKafkaListener;
import com.microservices.app.twitter.to.kafka.service.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class TwitterKafkaStreamImpl implements StreamRunner {

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
