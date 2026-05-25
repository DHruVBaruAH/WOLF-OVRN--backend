package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.InquiryStatus;
import com.Ishwarjit.Wolf_OVRN_backend.entity.ProductInquiry;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class InquiryResponse {
    private final UUID id;
    private final UserResponse user;
    private final ProductSummaryResponse product;
    private final InquiryStatus status;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public InquiryResponse(ProductInquiry entity) {
        this.id = entity.getId();
        this.user = UserResponse.from(entity.getUser());
        this.product = ProductSummaryResponse.from(entity.getProduct(), entity.getProduct().getImages());
        this.status = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
