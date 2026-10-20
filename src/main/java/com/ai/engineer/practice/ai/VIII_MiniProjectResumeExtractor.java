package com.ai.engineer.practice.ai;

import com.ai.engineer.practice.resumeExtractor.ResumeData;
import com.ai.engineer.practice.resumeExtractor.ResumeExtractor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VIII_MiniProjectResumeExtractor {

    private final ResumeExtractor resumeExtractor;

    public VIII_MiniProjectResumeExtractor(ResumeExtractor resumeExtractor) {
        this.resumeExtractor = resumeExtractor;
    }

    @PostMapping("/api/resume/extract")
    public ResumeData extract(@RequestBody ResumeRequest resumeRequest){
        return resumeExtractor.extractResumeData(resumeRequest.resumeText());
    }

    public record ResumeRequest(String resumeText){}
}
