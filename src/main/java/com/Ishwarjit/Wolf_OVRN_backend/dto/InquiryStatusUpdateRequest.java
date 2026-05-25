package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.InquiryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private InquiryStatus status;
}
