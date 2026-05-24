package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.FeatureCarouselRequest;
import com.Ishwarjit.Wolf_OVRN_backend.entity.FeatureCarousel;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.FeatureCarouselRepository;
import com.Ishwarjit.Wolf_OVRN_backend.service.CloudinaryService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/feature-carousel")
public class FeatureCarouselController {

    private final FeatureCarouselRepository carouselRepository;
    private final CloudinaryService cloudinaryService;

    public FeatureCarouselController(FeatureCarouselRepository carouselRepository, CloudinaryService cloudinaryService) {
        this.carouselRepository = carouselRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeatureCarousel>>> getActiveCarousels() {
        return ResponseEntity.ok(ApiResponse.ok(carouselRepository.findByIsActiveTrueOrderByDisplayOrderAsc()));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<FeatureCarousel>>> getAllCarousels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(limit, 1));
        return ResponseEntity.ok(ApiResponse.ok(carouselRepository.findAll(pageable)));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ApiResponse<FeatureCarousel>> createCarouselJson(
            @Valid @RequestBody FeatureCarouselRequest request) {
        if (request.getImage() == null || request.getImage().isBlank()) {
            throw new IllegalArgumentException("Image URL is required when not uploading a file");
        }
        FeatureCarousel carousel = new FeatureCarousel();
        carousel.setImage(request.getImage());
        carousel.setTitle(request.getTitle());
        carousel.setDescription(request.getDescription());
        carousel.setTagline(request.getTagline());
        if (request.getIsActive() != null) carousel.setIsActive(request.getIsActive());
        if (request.getDisplayOrder() != null) carousel.setDisplayOrder(request.getDisplayOrder());
        
        FeatureCarousel saved = carouselRepository.save(carousel);
        return ResponseEntity.ok(ApiResponse.ok(saved, "Carousel slide created successfully"));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FeatureCarousel>> createCarouselMultipart(
            @RequestPart("data") @Valid FeatureCarouselRequest request,
            @RequestPart("image") MultipartFile imageFile) throws IOException {
        
        validateImage(imageFile);
        String imageUrl = cloudinaryService.upload(imageFile);

        FeatureCarousel carousel = new FeatureCarousel();
        carousel.setImage(imageUrl);
        carousel.setTitle(request.getTitle());
        carousel.setDescription(request.getDescription());
        carousel.setTagline(request.getTagline());
        if (request.getIsActive() != null) carousel.setIsActive(request.getIsActive());
        if (request.getDisplayOrder() != null) carousel.setDisplayOrder(request.getDisplayOrder());
        
        FeatureCarousel saved = carouselRepository.save(carousel);
        return ResponseEntity.ok(ApiResponse.ok(saved, "Carousel slide created successfully"));
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<FeatureCarousel>> updateCarouselJson(
            @PathVariable UUID id,
            @Valid @RequestBody FeatureCarouselRequest request) {
        
        FeatureCarousel carousel = carouselRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carousel slide not found"));
                
        if (request.getImage() != null && !request.getImage().isBlank()) {
            carousel.setImage(request.getImage());
        }
        carousel.setTitle(request.getTitle());
        carousel.setDescription(request.getDescription());
        carousel.setTagline(request.getTagline());
        if (request.getIsActive() != null) carousel.setIsActive(request.getIsActive());
        if (request.getDisplayOrder() != null) carousel.setDisplayOrder(request.getDisplayOrder());
        
        FeatureCarousel updated = carouselRepository.save(carousel);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Carousel slide updated successfully"));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<FeatureCarousel>> updateCarouselMultipart(
            @PathVariable UUID id,
            @RequestPart("data") @Valid FeatureCarouselRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        
        FeatureCarousel carousel = carouselRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carousel slide not found"));
                
        if (imageFile != null && !imageFile.isEmpty()) {
            validateImage(imageFile);
            String imageUrl = cloudinaryService.upload(imageFile);
            carousel.setImage(imageUrl);
        } else if (request.getImage() != null && !request.getImage().isBlank()) {
            carousel.setImage(request.getImage());
        }

        carousel.setTitle(request.getTitle());
        carousel.setDescription(request.getDescription());
        carousel.setTagline(request.getTagline());
        if (request.getIsActive() != null) carousel.setIsActive(request.getIsActive());
        if (request.getDisplayOrder() != null) carousel.setDisplayOrder(request.getDisplayOrder());
        
        FeatureCarousel updated = carouselRepository.save(carousel);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Carousel slide updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCarousel(@PathVariable UUID id) {
        if (!carouselRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carousel slide not found");
        }
        
        carouselRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Carousel slide deleted successfully"));
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is missing or empty");
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds the limit of 10MB: " + file.getOriginalFilename());
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed: " + file.getOriginalFilename());
        }
    }
}
