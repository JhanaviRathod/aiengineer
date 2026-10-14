package com.ai.engineer.practice.prompt.engineering;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class I_ZeroShotPrompting {

    private final ChatClient chatClient;

    public I_ZeroShotPrompting(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    enum Sentiment{
        POSITIVE, NEGATIVE, NEUTRAL
    }

    @GetMapping("/api/zeroShotPrompting")
    public String zeroShotPrompting(){
        Sentiment reviewSentiment = chatClient.prompt("""
                Classify movie reviews as POSITIVE, NEUTRAL or NEGATIVE.
                Review: "Her" is a disturbing study revealing the direction
                humanity is headed if AI is allowed to keep evolving,
                unchecked. I wish there were more movies like this masterpiece.
                Sentiment:
                """)
                .options(ChatOptions.builder()
                        .model("gemini-3.5-flash-lite")
                        .temperature(0.1)
                        .maxTokens(200)).call()
                .entity(Sentiment.class);

        return reviewSentiment.name();
    }
}
