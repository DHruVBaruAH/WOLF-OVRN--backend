package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.InquiryRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.InquiryResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.InquiryStatusUpdateRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UserInquiriesGroupResponse;
import com.Ishwarjit.Wolf_OVRN_backend.service.ProductInquiryService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Ishwarjit.Wolf_OVRN_backend.exception.UnauthorizedException;

@RestController
@RequestMapping("/api/inquiries")
public class ProductInquiryController {

    private final ProductInquiryService inquiryService;

    public ProductInquiryController(ProductInquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    private String currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("No authenticated user");
        }
        return authentication.getPrincipal().toString();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InquiryResponse>> createInquiry(
            @Valid @RequestBody InquiryRequest request) {
        
        UUID userId = UUID.fromString(currentUserId());
        InquiryResponse response = inquiryService.createInquiry(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @GetMapping("/grouped")
    public ResponseEntity<ApiResponse<List<UserInquiriesGroupResponse>>> getGroupedInquiries() {
        return ResponseEntity.ok(ApiResponse.ok(inquiryService.getGroupedInquiries()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<InquiryResponse>>> getAllInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(limit, 1));
        Page<InquiryResponse> response = inquiryService.getAllInquiries(pageable);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<InquiryResponse>> updateInquiryStatus(
            @PathVariable UUID id,
            @Valid @RequestBody InquiryStatusUpdateRequest request) {
        
        InquiryResponse response = inquiryService.updateInquiryStatus(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Inquiry status updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInquiry(@PathVariable UUID id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Inquiry deleted successfully"));
    }
}
