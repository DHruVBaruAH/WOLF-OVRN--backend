package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.Cart;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CartResponse {

    private final UUID cartId;
    private final UUID userId;
    private final List<CartItemResponse> items;
    private final BigDecimal total;

    private CartResponse(UUID cartId, UUID userId, List<CartItemResponse> items, BigDecimal total) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.total = total;
    }

    public static CartResponse from(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartItemResponse::from)
                .toList();
        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(cart.getId(), cart.getUser().getId(), items, total);
    }
}
