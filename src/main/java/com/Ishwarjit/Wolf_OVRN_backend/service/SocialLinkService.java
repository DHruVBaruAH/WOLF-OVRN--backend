package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.SocialLinkResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateSocialLinkRequest;
import com.Ishwarjit.Wolf_OVRN_backend.entity.SocialLink;
import com.Ishwarjit.Wolf_OVRN_backend.repository.SocialLinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialLinkService {

    private final SocialLinkRepository socialLinkRepository;

    public SocialLinkService(SocialLinkRepository socialLinkRepository) {
        this.socialLinkRepository = socialLinkRepository;
    }

    private SocialLink getOrCreate() {
        return socialLinkRepository.findAll().stream().findFirst().orElseGet(() -> {
            SocialLink socialLink = new SocialLink();
            return socialLinkRepository.save(socialLink);
        });
    }

    @Transactional(readOnly = true)
    public SocialLinkResponse getSocialLinks() {
        SocialLink socialLink = getOrCreate();
        return SocialLinkResponse.builder()
                .instagramUrl(socialLink.getInstagramUrl())
                .xUrl(socialLink.getXUrl())
                .linkedinUrl(socialLink.getLinkedinUrl())
                .build();
    }

    @Transactional
    public SocialLinkResponse updateSocialLinks(UpdateSocialLinkRequest request) {
        SocialLink socialLink = getOrCreate();
        
        if (request.getInstagramUrl() != null) {
            socialLink.setInstagramUrl(request.getInstagramUrl());
        }
        if (request.getXUrl() != null) {
            socialLink.setXUrl(request.getXUrl());
        }
        if (request.getLinkedinUrl() != null) {
            socialLink.setLinkedinUrl(request.getLinkedinUrl());
        }
        
        socialLink = socialLinkRepository.save(socialLink);
        
        return SocialLinkResponse.builder()
                .instagramUrl(socialLink.getInstagramUrl())
                .xUrl(socialLink.getXUrl())
                .linkedinUrl(socialLink.getLinkedinUrl())
                .build();
    }
}
