package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.CreateSizeRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.SizeDto;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateSizeRequest;
import com.Ishwarjit.Wolf_OVRN_backend.service.SizeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sizes")
public class SizeController {

    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SizeDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(sizeService.getAll()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ApiResponse<SizeDto>> create(@Valid @RequestBody CreateSizeRequest request) {
        SizeDto created = sizeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<SizeDto>> update(@PathVariable UUID id, @Valid @RequestBody UpdateSizeRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(sizeService.update(id, request), "Size updated successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        sizeService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Size deleted successfully"));
    }
}
