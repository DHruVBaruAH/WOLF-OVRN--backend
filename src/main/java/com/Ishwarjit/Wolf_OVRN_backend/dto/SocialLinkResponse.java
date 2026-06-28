package com.Ishwarjit.Wolf_OVRN_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinkResponse {
    private String instagramUrl;
    private String xUrl;
    private String linkedinUrl;
}
