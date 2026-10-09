package com.ai.engineer.practice.ai;

import com.ai.engineer.practice.service.FallBackMechanism;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class I_DevsAiGeneralSupportController {

    private static final String SYSTEM_PROMPT = """
            You give suggestion for names.
            1. You must give name suggestion in the form of list.
            2. Make list of 5 points.
            3. Give only names don't give other information on names.
            """ ;
    private static final Logger log = LoggerFactory.getLogger(I_DevsAiGeneralSupportController.class);

    private final ChatClient chatClient;

    public I_DevsAiGeneralSupportController(@Qualifier("chatClient")  ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // Added error handling and returning safe error message
    @GetMapping("/api/ask")
    public ResponseEntity<String> askAi(@RequestParam String question){
        try{
            String prompt = question;

            String response =  chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(prompt)
                    .call()
                    .content();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("The AI service is temporarily unavailable.");
        }
    }

}
