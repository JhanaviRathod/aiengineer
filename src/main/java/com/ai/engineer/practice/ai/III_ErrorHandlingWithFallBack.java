package com.ai.engineer.practice.ai;

import com.ai.engineer.practice.service.FallBackMechanism;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class III_ErrorHandlingWithFallBack {
    private final FallBackMechanism callLlm;

    public III_ErrorHandlingWithFallBack(FallBackMechanism callLlm) {
        this.callLlm = callLlm;
    }

    // Add error handling with fallback behavior
    @GetMapping("/api/askWithFallback")
    public String ask(@RequestParam String prompt) {
        // The controller executes a clean service call
        return callLlm.askWithFallback(prompt);
    }
}
