package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.CreateProductRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.ImageUploadResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.ProductDetailResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.ProductSummaryResponse;
import com.Ishwarjit.Wolf_OVRN_backend.service.ProductService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductSummaryResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        return ResponseEntity.ok(productService.list(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDetailResponse> create(@Valid @RequestBody CreateProductRequest request) {
        ProductDetailResponse created = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file) throws IOException {
        ImageUploadResponse response = productService.addImage(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
