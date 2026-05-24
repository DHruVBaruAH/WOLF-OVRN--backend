package com.Ishwarjit.Wolf_OVRN_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeatureCarouselRequest {
    private String image;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    
    private String tagline;
    
    private Boolean isActive;
    
    private Integer displayOrder;
}
