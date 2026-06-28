package com.Ishwarjit.Wolf_OVRN_backend.repository;

import com.Ishwarjit.Wolf_OVRN_backend.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, UUID> {
}
