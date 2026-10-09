package com.ai.engineer.practice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FallBackMechanism {

    private final ChatClient chatClient;
    private final ChatClient secondaryChatClient;
    private static final Logger log = LoggerFactory.getLogger(FallBackMechanism.class);


    public FallBackMechanism(
            @Qualifier("chatClient") ChatClient chatClient,
            @Qualifier("secondaryChatClient") ChatClient secondaryChatClient
    ){
        this.chatClient = chatClient;
        this.secondaryChatClient = chatClient;
    }

    public String askWithFallback(String prompt) {
        try {
            // 1. Try calling your custom LLM bean first
            return chatClient.prompt(prompt).call().content();
        } catch (Exception e) {
            // 2. If it fails, instantly fall back to the application.yml configured LLM
            log.warn("Primary LLM failed due to: {}. Falling back to secondary LLM...", e.getMessage());
            return secondaryChatClient.prompt(prompt).call().content();
        }
    }
}
