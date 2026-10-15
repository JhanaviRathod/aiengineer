package com.ai.engineer.practice.prompt.engineering;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class III_SystemContextualRolePrompting {

    private final ChatClient chatClient;

    public III_SystemContextualRolePrompting(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // System Prompting
    @GetMapping("/api/systemPrompting")
    public MovieReview systemPrompting(){

        return chatClient.prompt()
                .system("""
                        Classify movie reviews as positive, neutral or negative. 
                        Return valid JSON.
                        """)
                .user("""
                        Review: "Her" is a disturbing study revealing the direction
                        humanity is headed if AI is allowed to keep evolving,
                        unchecked. It's so disturbing I couldn't watch it.
                        
                        JSON Response:
                        """)
                .call()
                .entity(MovieReview.class);
    }

    record MovieReview(Movie[] movie_reviews){
        enum Sentiment{
            NEUTRAL, POSITIVE, NEGATIVE
        }

        record Movie(Sentiment sentiment, String name){
        }
    }

    // Role Prompting enhanced with style instructions:
    @GetMapping("/api/rolePrompting")
    public String rolePrompting() {
        String humorousTravelSuggestions = chatClient
                .prompt()
                .system("""
                        I want you to act as a travel guide. I will write to you about
                        my location and you will suggest 3 places to visit near me in
                        a humorous style.
                        """)
                .user("""
                        My suggestion: "I am in Amsterdam and I want to visit only museums."
                        Travel Suggestions:
                        """)
                .call()
                .content();
        return humorousTravelSuggestions;
    }

    @GetMapping("/api/contextPrompting")
    public String contextPrompting(){
        return chatClient
                .prompt()
                .user(u -> u.text("""
                    Suggest 3 topics to write an article about with a few lines of
                    description of what this article should contain.

                    Context: {context}
                    """)
                        .param("context", "You are writing for a blog about retro 80's arcade video games."))
                .call()
                .content();
    }

}
