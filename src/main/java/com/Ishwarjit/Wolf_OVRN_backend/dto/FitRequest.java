package com.Ishwarjit.Wolf_OVRN_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FitRequest {

    @NotBlank(message = "Fit name is required")
    @Size(max = 255)
    private String name;

    private String description;
}
