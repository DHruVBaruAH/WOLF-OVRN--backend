package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.Size;
import java.util.UUID;

public record SizeDto(UUID id, String sizeName) {
    public static SizeDto from(Size size) {
        if (size == null) {
            return null;
        }
        return new SizeDto(size.getId(), size.getSizeName());
    }
}
