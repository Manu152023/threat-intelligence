package com.threatintel.dto;

import com.threatintel.model.Category;
import com.threatintel.model.Severity;
import com.threatintel.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ThreatRequest(
        @NotBlank(message = "Title is required") @Size(max = 255, message = "Title must be at most 255 characters") String title,
        @NotBlank(message = "Description is required") @Size(max = 4000, message = "Description must be at most 4000 characters") String description,
        @NotNull(message = "Severity is required") Severity severity,
        @NotNull(message = "Category is required") Category category,
        @NotNull(message = "Status is required") Status status,
        @Size(max = 100, message = "Source must be at most 100 characters") String source) {
}
