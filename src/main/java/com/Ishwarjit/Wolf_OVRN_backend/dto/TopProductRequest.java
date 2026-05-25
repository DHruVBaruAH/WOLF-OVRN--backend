package com.Ishwarjit.Wolf_OVRN_backend.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopProductRequest {
    @NotNull(message = "Product ID is required")
    private UUID productId;
    private int displayOrder = 0;
}
