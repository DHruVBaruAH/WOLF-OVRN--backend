package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.TopProductRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.TopProductResponse;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Product;
import com.Ishwarjit.Wolf_OVRN_backend.entity.TopProduct;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ProductRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.TopProductRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopProductService {

    private final TopProductRepository topProductRepository;
    private final ProductRepository productRepository;

    public TopProductService(TopProductRepository topProductRepository, ProductRepository productRepository) {
        this.topProductRepository = topProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<TopProductResponse> getTopProducts() {
        return topProductRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(TopProductResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TopProductResponse addTopProduct(TopProductRequest request) {
        if (topProductRepository.existsByProductId(request.getProductId())) {
            throw new IllegalArgumentException("Product is already in the top products list");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        TopProduct topProduct = new TopProduct();
        topProduct.setProduct(product);
        topProduct.setDisplayOrder(request.getDisplayOrder());

        TopProduct saved = topProductRepository.save(topProduct);
        return new TopProductResponse(saved);
    }

    @Transactional
    public TopProductResponse updateTopProduct(UUID id, TopProductRequest request) {
        TopProduct topProduct = topProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Top product entry not found"));

        // Allow changing product ID, but verify if the new one already exists
        if (!topProduct.getProduct().getId().equals(request.getProductId())) {
            if (topProductRepository.existsByProductId(request.getProductId())) {
                throw new IllegalArgumentException("Product is already in the top products list");
            }
            Product newProduct = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            topProduct.setProduct(newProduct);
        }

        topProduct.setDisplayOrder(request.getDisplayOrder());

        TopProduct saved = topProductRepository.save(topProduct);
        return new TopProductResponse(saved);
    }

    @Transactional
    public void removeTopProduct(UUID id) {
        if (!topProductRepository.existsById(id)) {
            throw new ResourceNotFoundException("Top product entry not found");
        }
        topProductRepository.deleteById(id);
    }
}
