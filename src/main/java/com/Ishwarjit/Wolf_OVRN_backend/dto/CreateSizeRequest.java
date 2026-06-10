package com.Ishwarjit.Wolf_OVRN_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSizeRequest {
    @NotBlank(message = "Size name is required")
    private String sizeName;
}
