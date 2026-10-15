package com.ai.engineer.practice.prompt.engineering;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VI_SelfConsistencyPrompting {

    private final ChatClient chatClient;

    public VI_SelfConsistencyPrompting(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/selfConsistencyPrompting")
    public String selfConsistencyPrompting(){
        String email = """
            Hi,
            I have seen you use Wordpress for your website. A great open
            source content management system. I have used it in the past
            too. It comes with lots of great user plugins. And it's pretty
            easy to set up.
            I did notice a bug in the contact form, which happens when
            you select the name field. See the attached screenshot of me
            entering text in the name field. Notice the JavaScript alert
            box that I inv0k3d.
            But for the rest it's a great website. I enjoy reading it. Feel
            free to leave the bug in the website, because it gives me more
            interesting things to read.
            Cheers,
            Harry the Hacker.
            """;

        record EmailClassification(Classification classification, String reasoning) {
            enum Classification {
                IMPORTANT, NOT_IMPORTANT
            }
        }

        int importantCount = 0;
        int nonImportantCount = 0;

        // Run the model for 5 times
        for (int i = 0; i < 5; i++) {
            EmailClassification output = chatClient.prompt()
                    .options(OpenAiChatOptions.builder()
                            .temperature(1.0))
                    .user(u -> u.text("""
                            Email: {email}
                            Classify the above email as IMPORTANT or NOT IMPORTANT. Let's
                            think step by step and explain why.
                            """).param("email", email))
                    .call()
                    .entity(EmailClassification.class);

            if(output.classification() == EmailClassification.Classification.NOT_IMPORTANT){
                nonImportantCount++;
            } else if(output.classification() == EmailClassification.Classification.IMPORTANT){
                importantCount++;
            }
        }
        return importantCount > nonImportantCount ? "IMPORTANT" : "NOT IMPORTANT";
    }
}
