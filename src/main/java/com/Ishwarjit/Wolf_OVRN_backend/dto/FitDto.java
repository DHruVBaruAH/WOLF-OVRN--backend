package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.Fit;
import java.util.UUID;

public record FitDto(
        UUID id,
        String name,
        String description) {

    public static FitDto from(Fit fit) {
        if (fit == null) {
            return null;
        }
        return new FitDto(
                fit.getId(),
                fit.getName(),
                fit.getDescription());
    }
}
