package com.ai.engineer.practice.resumeExtractor.Exception;

public class ResumeExtractorException extends RuntimeException{

    public ResumeExtractorException(String message){
        super(message);
    }

    public ResumeExtractorException(String message, Throwable cause) {
        super(message, cause);
    }
}
