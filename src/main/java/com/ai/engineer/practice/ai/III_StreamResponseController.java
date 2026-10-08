package com.ai.engineer.practice.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class III_StreamResponseController {

    private final ChatClient chatClient;

    public III_StreamResponseController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

//     http --stream GET ":8080/api/getStreamResponse?question=Tell%20me%20a%20story"
    @GetMapping("/api/getStreamResponse")
    public Flux<String> getStreamResponse(@RequestParam String question) {
        Flux<String> response = chatClient.prompt()
                .user(question)
                .stream().content();
        return response;
    }
}
