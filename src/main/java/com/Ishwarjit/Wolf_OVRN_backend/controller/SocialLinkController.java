package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.SocialLinkResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateSocialLinkRequest;
import com.Ishwarjit.Wolf_OVRN_backend.service.SocialLinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/social-links")
public class SocialLinkController {

    private final SocialLinkService socialLinkService;

    public SocialLinkController(SocialLinkService socialLinkService) {
        this.socialLinkService = socialLinkService;
    }

    /**
     * [PUBLIC] Get social media links.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<SocialLinkResponse>> getSocialLinks() {
        return ResponseEntity.ok(ApiResponse.ok(socialLinkService.getSocialLinks()));
    }

    /**
     * [ADMIN] Update social media links.
     */
    @PatchMapping
    public ResponseEntity<ApiResponse<SocialLinkResponse>> updateSocialLinks(
            @RequestBody UpdateSocialLinkRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(socialLinkService.updateSocialLinks(request), "Social links updated successfully"));
    }
}
