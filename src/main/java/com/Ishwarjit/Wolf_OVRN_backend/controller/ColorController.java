package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.ColorDto;
import com.Ishwarjit.Wolf_OVRN_backend.dto.CreateColorRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateColorRequest;
import com.Ishwarjit.Wolf_OVRN_backend.service.ColorService;
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
@RequestMapping("/api/colors")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ColorDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(colorService.getAll()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ApiResponse<ColorDto>> create(@Valid @RequestBody CreateColorRequest request) {
        ColorDto created = colorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<ApiResponse<ColorDto>> update(@PathVariable UUID id, @Valid @RequestBody UpdateColorRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(colorService.update(id, request), "Color updated successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        colorService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Color deleted successfully"));
    }
}
