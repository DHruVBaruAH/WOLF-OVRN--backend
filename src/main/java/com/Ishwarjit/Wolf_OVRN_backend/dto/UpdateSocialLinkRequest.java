package com.Ishwarjit.Wolf_OVRN_backend.dto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UpdateSocialLinkRequest {
    private String instagramUrl;
    
    @JsonProperty("xUrl")
    private String xUrl;
    
    private String linkedinUrl;
}
