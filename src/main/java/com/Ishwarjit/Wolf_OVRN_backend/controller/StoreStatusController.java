package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.StoreStatusResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateStoreStatusRequest;
import com.Ishwarjit.Wolf_OVRN_backend.service.StoreStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StoreStatusController {

    private final StoreStatusService storeStatusService;

    public StoreStatusController(StoreStatusService storeStatusService) {
        this.storeStatusService = storeStatusService;
    }

    /**
     * [PUBLIC] Very lightweight endpoint for the frontend to check if the store is open,
     * under maintenance, or has a special message.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<StoreStatusResponse>> getStatus() {
        return ResponseEntity.ok(ApiResponse.ok(storeStatusService.getStatus()));
    }

    /**
     * [ADMIN] Update the store status flags and messages.
     */
    @PatchMapping
    public ResponseEntity<ApiResponse<StoreStatusResponse>> updateStatus(
            @RequestBody UpdateStoreStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(storeStatusService.updateStatus(request), "Store status updated successfully"));
    }
}
