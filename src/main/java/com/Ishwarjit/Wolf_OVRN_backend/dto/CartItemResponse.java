package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.CartItem;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Product;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CartItemResponse {

    private final UUID cartItemId;
    private final ProductSummaryResponse product;
    private final int quantity;
    private final BigDecimal subtotal;

    private CartItemResponse(UUID cartItemId, ProductSummaryResponse product, int quantity, BigDecimal subtotal) {
        this.cartItemId = cartItemId;
        this.product = product;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public static CartItemResponse from(CartItem item) {
        Product product = item.getProduct();
        BigDecimal subtotal = product.getSellingPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getId(),
                ProductSummaryResponse.from(product, product.getImages()),
                item.getQuantity(),
                subtotal);
    }
}
