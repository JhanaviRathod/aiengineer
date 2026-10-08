package com.ai.engineer.practice.ai;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class II_PromptTemplatingController {

    private final ChatModel chatModel;


    public II_PromptTemplatingController(ChatModel chatModel){
        this.chatModel = chatModel;
    }

    @GetMapping("/api/promptTemplate")
    public String getPromptTemplateResponse(@RequestParam String adjective, @RequestParam String topic){
        PromptTemplate promptTemplate =  new PromptTemplate("Tell me a {adjective} joke about {topic}");

        Prompt prompt = promptTemplate.create(Map.of("adjective", adjective, "topic", topic));

        return chatModel.call(prompt).getResult().toString();
    }

    @GetMapping("/api/promptRoles")
    public String getPromptRolesResponse(@RequestParam String request, @RequestParam String name, @RequestParam String voice){

        Message userMessage = new UserMessage(request);

        String systemText = """
                You are a helpful AI assistant that helps people find information.
                Your name is {name}
                You should reply to the user's request with your name and also in the style of a {voice}.
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        return chatModel.call(prompt).getResult().toString();
    }

    @GetMapping("/api/customTemplate")
    public String getCustomTemplateRender(@RequestParam String composer){
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
            Tell me the names of 5 movies whose soundtrack was composed by <composer>.
            """).build();

        String prompt = promptTemplate.render(Map.of("composer", composer));

        return chatModel.call(prompt).toString();
    }


}
