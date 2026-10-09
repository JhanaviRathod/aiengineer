package com.ai.engineer.practice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class llmConfig {

    @Bean("chatClient")
    @Primary
    public ChatClient chatClient(@Qualifier("primaryChatModel") OpenAiChatModel model){
        return ChatClient.builder(model).build();
    }

    @Bean("primaryChatModel")
    public OpenAiChatModel primaryChatModel(@Value("${app.ai.models.primary.base-url}") String url,
                                            @Value("${app.ai.models.primary.api-key}") String key,
                                            @Value("${app.ai.models.primary.model}") String model){
        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(key)
                        .baseUrl(url)
                        .model(model)
                        .maxRetries(2)
                        .build()).build();
    }

    @Bean("secondaryChatClient")
    public ChatClient secondaryChatClient(
            @Qualifier("secondaryChatModel") OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean("secondaryChatModel")
    public OpenAiChatModel secondaryChatModel(
            @Value("${app.ai.models.secondary.base-url}") String url,
            @Value("${app.ai.models.secondary.api-key}") String key,
            @Value("${app.ai.models.secondary.model}") String model) {

        return OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .apiKey(key)
                        .baseUrl(url)
                        .model(model)
                        .maxRetries(1)
                        .build())
                .build();
    }
}
