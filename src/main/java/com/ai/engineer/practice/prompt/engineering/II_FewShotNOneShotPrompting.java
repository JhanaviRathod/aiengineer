package com.ai.engineer.practice.prompt.engineering;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class II_FewShotNOneShotPrompting {

    private final ChatClient chatClient;

    private static final String PROMPT = """
            Parse a customer's pizza order into a valid JSON 
            
            Example 1:
            I want a small pizza with cheese, tomato sauce and pepperoni.
            JSON Response:
            ```
            {
                "size": "small",
                "type": "normal",
                "ingredients" : ["cheese", "tomato sauce", "pepperoni"]
            }
            ```
            
            Example 2: 
            Can I get a large pizza with tomato sauce, basil and mozzarella.
            JSON Response:
            ```
            {
                "size": "large",
                "type" : "normal",
                "ingredients": ["tomato sauce", "basil", "mozzarella"]
            }
            ```
            
            Now, I would like a large pizza, with the first half cheese and mozzarella.
            And the other tomato sauce, ham and pineapple.
            """;

    public II_FewShotNOneShotPrompting(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/fewShotPrompting")
    public String fewShotPrompting(){
        String pizzaOrder = chatClient.prompt(PROMPT)
                .options(ChatOptions.builder()
                        .model("gemini-3.5-flash-lite")
                        .temperature(0.1)
                        .maxTokens(250))
                .call().content();

        return pizzaOrder;
    }
}
