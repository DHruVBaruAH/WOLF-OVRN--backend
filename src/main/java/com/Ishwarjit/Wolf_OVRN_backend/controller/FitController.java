package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.FitDto;
import com.Ishwarjit.Wolf_OVRN_backend.dto.FitRequest;
import com.Ishwarjit.Wolf_OVRN_backend.service.FitService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fits")
public class FitController {

    private final FitService fitService;

    public FitController(FitService fitService) {
        this.fitService = fitService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FitDto>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.ok(fitService.listAll(page, limit)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FitDto>> create(@Valid @RequestBody FitRequest request) {
        FitDto response = fitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FitDto>> update(
            @PathVariable UUID id, 
            @Valid @RequestBody FitRequest request) {
        FitDto response = fitService.update(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Fit updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        fitService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Fit deleted successfully"));
    }
}
