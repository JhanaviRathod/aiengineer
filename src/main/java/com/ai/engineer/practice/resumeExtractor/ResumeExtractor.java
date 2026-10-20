package com.ai.engineer.practice.resumeExtractor;

import com.ai.engineer.practice.resumeExtractor.Exception.ResumeExtractorException;
import jakarta.validation.Validator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ResumeExtractor {

    private final ChatClient chatClient;
    private final Validator validator;

    public ResumeExtractor(ChatClient chatClient, Validator validator) {
        this.chatClient = chatClient;
        this.validator = validator;
    }

    public ResumeData extractResumeData(String resumeText){
        if(resumeText == null || resumeText.isEmpty()){
            throw new IllegalArgumentException("Resume Text must not be empty");
        }

        ResumeData result;

        try {
            result = chatClient.prompt()
                    .system(ResumePrompts.V1)
                    .user(u -> u.text("""
                            Extract the resume information from this text:
                            
                            {resumeText}
                            """).param("resumeText", resumeText))
                    .call()
                    .entity(ResumeData.class, s -> s.validateSchema());

        }
        catch (RuntimeException ex){
            throw new ResumeExtractorException(
                    "Unable to extract structured resume data", ex);
        }

        if(result == null){
            throw new ResumeExtractorException("The model returned no structured result");
        }

        return result;
    }
}
