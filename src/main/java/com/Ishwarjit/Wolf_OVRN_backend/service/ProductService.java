package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.CreateProductRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.ImageUploadResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.ProductDetailResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.ProductSummaryResponse;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Category;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Product;
import com.Ishwarjit.Wolf_OVRN_backend.entity.ProductImage;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.CategoryRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ProductImageRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ProductRepository;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductImageRepository productImageRepository,
            CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional(readOnly = true)
    public Page<ProductSummaryResponse> list(Pageable pageable) {
        return productRepository.findAll(pageable).map(product -> {
            List<ProductImage> images = productImageRepository
                    .findByProductIdOrderByDisplayOrderAsc(product.getId());
            return ProductSummaryResponse.from(product, images);
        });
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        List<ProductImage> images = productImageRepository
                .findByProductIdOrderByDisplayOrderAsc(id);
        return ProductDetailResponse.from(product, images);
    }

    @Transactional
    public ProductDetailResponse create(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setBasePrice(request.getBasePrice());
        product.setCompareAtPrice(request.getCompareAtPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setIsActive(true);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }

        Product saved = productRepository.save(product);
        return ProductDetailResponse.from(saved, List.of());
    }

    @Transactional
    public ImageUploadResponse addImage(UUID productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        String secureUrl = cloudinaryService.upload(file);
        long existingCount = productImageRepository.countByProductId(productId);

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setUrl(secureUrl);
        image.setAltText(product.getName());
        image.setIsPrimary(existingCount == 0);
        image.setDisplayOrder((int) existingCount);

        ProductImage saved = productImageRepository.save(image);
        return ImageUploadResponse.from(saved);
    }
}
