package com.ai.engineer.practice.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class VII_StructuredOutput {

    private final ChatModel chatModel;
    private final ChatClient chatClient;

    public VII_StructuredOutput(@Qualifier("primaryChatModel") ChatModel chatModel, ChatClient chatClient) {
        this.chatModel = chatModel;
        this.chatClient = chatClient;
    }

    record CodeReviewResponse(String summary,
                                     int qualityScore,
                                     List<String> detectedIssues,
                                     List<String> suggestedFix){}

    @PostMapping("/api/beanOutputConverter")
    public CodeReviewResponse beanOutputConverter(@RequestBody String code){

        BeanOutputConverter<CodeReviewResponse> converter =new BeanOutputConverter<>(CodeReviewResponse.class);

        String template = """
                You are in expert in java Code Reviewer
                Review the following java code snippet
                
                {code}
                
                {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("code", code,
                "format", converter.getFormat()));

        String response = chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getText();

        return converter.convert(response);
    }

    record ActorsFilm(String actor, List<String> movies){}

    @GetMapping("/api/structuredOutput")
    public ActorsFilm structuredOutput(@RequestParam String actor){
        return chatClient.prompt()
                .user(s -> s.text("Generate the filmography for {actor}").param("actor", actor))
                .call().entity(ActorsFilm.class, spec -> spec.validateSchema());
    }
}
