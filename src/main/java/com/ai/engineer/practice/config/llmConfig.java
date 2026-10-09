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

    @Bean
    @Primary
    public ChatClient chatClient(@Qualifier("chatClient") @Value("${app.ai.models.primary.base-url}") String url,
                                 @Value("${app.ai.models.primary.api-key}") String key,
                                 @Value("${app.ai.models.primary.model}") String model,
                                 ChatClient.Builder builder){
        return builder.defaultOptions(OpenAiChatOptions.builder().apiKey(key)
                .baseUrl(url)
                .model(model)
                .maxRetries(2)).build();
    }

    @Bean
    public ChatClient secondarChatClient(@Qualifier("secondaryChatClient") @Value("${app.ai.models.secondary.base-url}") String url,
                                  @Value("${app.ai.models.secondary.api-key}") String key,
                                  @Value("${app.ai.models.secondary.model}") String model,
                                  ChatClient.Builder builder){
        return builder.defaultOptions(OpenAiChatOptions.builder().apiKey(key)
                .baseUrl(url)
                .model(model)
                .maxRetries(1)).build();
    }
}
