package com.ai.engineer.practice.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VI_SystemUserInstruction {

    private final ChatClient chatClientWithDefaultSystemMessage;
    private final ChatClient chatClientWithDefaultSystemMessageWithParameters;

    public VI_SystemUserInstruction(
            @Qualifier("chatClientWithDefaultSystemMessage") ChatClient chatClientWithDefaultSystemMessage,
            @Qualifier("chatClientWithDefaultSystemMessageWithParameters") ChatClient chatClientWithDefaultSystemMessageWithParameters) {
        this.chatClientWithDefaultSystemMessage = chatClientWithDefaultSystemMessage;
        this.chatClientWithDefaultSystemMessageWithParameters = chatClientWithDefaultSystemMessageWithParameters;
    }

    // System instruction with default system prompt
    @GetMapping("/api/askWithDefaultSystemMessage")
    public ResponseEntity<String> askWithDefaultSystemMessage(@RequestParam String question) {
            String prompt = question;

            String response = chatClientWithDefaultSystemMessage.prompt()
                    .user(prompt)
                    .call()
                    .content();

            return ResponseEntity.ok(response);
    }

    // System instruction with default system prompt with parameters
    @GetMapping("/api/askWithDefaultSystemMessageWithParameters")
    public ResponseEntity<String> askWithDefaultSystemMessageWithParameters(@RequestParam String question, @RequestParam String voice) {
        String prompt = question;

        String response = chatClientWithDefaultSystemMessageWithParameters.prompt()
                .system(sp -> sp.param("voice", voice))
                .user(prompt)
                .call()
                .content();

        return ResponseEntity.ok(response);
    }
}
