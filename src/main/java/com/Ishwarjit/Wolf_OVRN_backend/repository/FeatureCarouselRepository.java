package com.Ishwarjit.Wolf_OVRN_backend.repository;

import com.Ishwarjit.Wolf_OVRN_backend.entity.FeatureCarousel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureCarouselRepository extends JpaRepository<FeatureCarousel, UUID> {
    List<FeatureCarousel> findByIsActiveTrueOrderByDisplayOrderAsc();
    Page<FeatureCarousel> findAll(Pageable pageable);
}
