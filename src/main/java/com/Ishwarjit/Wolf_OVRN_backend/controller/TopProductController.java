package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.TopProductRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.TopProductResponse;
import com.Ishwarjit.Wolf_OVRN_backend.service.TopProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/top-products")
public class TopProductController {

    private final TopProductService topProductService;

    public TopProductController(TopProductService topProductService) {
        this.topProductService = topProductService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopProductResponse>>> getTopProducts() {
        return ResponseEntity.ok(ApiResponse.ok(topProductService.getTopProducts()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TopProductResponse>> addTopProduct(@Valid @RequestBody TopProductRequest request) {
        TopProductResponse response = topProductService.addTopProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TopProductResponse>> updateTopProduct(
            @PathVariable UUID id, 
            @Valid @RequestBody TopProductRequest request) {
        TopProductResponse response = topProductService.updateTopProduct(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Top product updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removeTopProduct(@PathVariable UUID id) {
        topProductService.removeTopProduct(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Top product removed successfully"));
    }
}
