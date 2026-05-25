package com.Ishwarjit.Wolf_OVRN_backend.repository;

import com.Ishwarjit.Wolf_OVRN_backend.entity.TopProduct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopProductRepository extends JpaRepository<TopProduct, UUID> {
    List<TopProduct> findAllByOrderByDisplayOrderAsc();
    Optional<TopProduct> findByProductId(UUID productId);
    boolean existsByProductId(UUID productId);
}
