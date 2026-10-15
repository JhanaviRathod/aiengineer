package com.ai.engineer.practice.prompt.engineering;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IV_StepBackPrompting {

    private final ChatClient chatClient;

    public IV_StepBackPrompting(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/stepBackPrompting")
    public String stepBackPrompting(){
        String stepBackContext = chatClient.prompt("""
                Based on popular first-person shooter action games, what are
                5 fictional key settings that contribute to a challenging and
                engaging level storyline in a first-person shooter video game?
                """).call().content();

        String response = chatClient.prompt().user(u -> u.text("""
                Write a one paragraph storyline for a new level of a first-
                person shooter video game that is challenging and engaging.
                
                Context: {stepBackContext}
                """).param("stepBackContext", stepBackContext)).call().content();

        return response;
    }
}
