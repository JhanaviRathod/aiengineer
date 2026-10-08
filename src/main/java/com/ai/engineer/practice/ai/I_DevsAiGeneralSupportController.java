package com.ai.engineer.practice.ai;

import org.springframework.ai.chat.client.ChatClient;
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

    private final ChatClient chatClient;

    public I_DevsAiGeneralSupportController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    @GetMapping("/api/ask")
    public String askAi(@RequestParam String question){

        String prompt = question;

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(prompt)
                .call()
                .content();

    }

}
