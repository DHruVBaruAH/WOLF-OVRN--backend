package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.Color;
import java.util.UUID;

public record ColorDto(UUID id, String colorName, String hexCode) {
    public static ColorDto from(Color color) {
        if (color == null) {
            return null;
        }
        return new ColorDto(color.getId(), color.getColorName(), color.getHexCode());
    }
}
