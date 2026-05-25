package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.TopProduct;
import java.util.UUID;
import lombok.Getter;

@Getter
public class TopProductResponse {
    private final UUID id;
    private final int displayOrder;
    private final ProductSummaryResponse product;

    public TopProductResponse(TopProduct entity) {
        this.id = entity.getId();
        this.displayOrder = entity.getDisplayOrder();
        this.product = ProductSummaryResponse.from(entity.getProduct(), entity.getProduct().getImages());
    }
}
