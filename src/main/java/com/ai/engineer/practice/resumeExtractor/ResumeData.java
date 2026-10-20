package com.ai.engineer.practice.resumeExtractor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ResumeData(
        String fullName,
        @Email
        String email,
        String phone,
        String currentTitle,
        @DecimalMin("0.0")
        BigDecimal experienceYears,
        List<String> skills,
        List<@Valid Education> education,
        List<@Valid WorkExperience> workExperiences,
        List<@NotNull String> certifications

) {
    public record Education(String degree,
                            String institution,
                            String graduationYear){}

    public record WorkExperience(String company,
                                 String jobTitle,
                                 @DecimalMin("0.0")
                                 BigDecimal experienceYears,
                                 List<String> responsibilities){}
}

