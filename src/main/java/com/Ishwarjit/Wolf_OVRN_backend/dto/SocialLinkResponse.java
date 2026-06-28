package com.Ishwarjit.Wolf_OVRN_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinkResponse {
    private String instagramUrl;
    
    @JsonProperty("xUrl")
    private String xUrl;
    
    private String linkedinUrl;
}
