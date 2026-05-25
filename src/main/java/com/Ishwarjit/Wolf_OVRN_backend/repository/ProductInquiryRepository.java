package com.Ishwarjit.Wolf_OVRN_backend.repository;

import com.Ishwarjit.Wolf_OVRN_backend.entity.ProductInquiry;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInquiryRepository extends JpaRepository<ProductInquiry, UUID> {
    Page<ProductInquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<ProductInquiry> findAllByOrderByCreatedAtDesc();
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
}
