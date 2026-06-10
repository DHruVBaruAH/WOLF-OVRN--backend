package com.Ishwarjit.Wolf_OVRN_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateColorRequest {
    @NotBlank(message = "Color name is required")
    private String colorName;

    @NotBlank(message = "Hex code is required")
    private String hexCode;
}
