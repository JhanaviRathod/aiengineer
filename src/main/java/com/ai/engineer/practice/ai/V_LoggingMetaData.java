package com.ai.engineer.practice.ai;

import com.ai.engineer.practice.service.FallBackMechanism;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class V_LoggingMetaData {

    private final FallBackMechanism callLlm;

    public V_LoggingMetaData(FallBackMechanism callLlm) {
        this.callLlm = callLlm;
    }

    @GetMapping("/api/askWithMetaData")
    public String askWithMetaData(@RequestParam String prompt) {
        // The controller executes a clean service call
        return callLlm.callModel(prompt);
    }
}
