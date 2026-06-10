package com.Ishwarjit.Wolf_OVRN_backend.service;

import com.Ishwarjit.Wolf_OVRN_backend.dto.CartItemRequest;
import com.Ishwarjit.Wolf_OVRN_backend.dto.CartResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UpdateCartItemRequest;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Cart;
import com.Ishwarjit.Wolf_OVRN_backend.entity.CartItem;
import com.Ishwarjit.Wolf_OVRN_backend.entity.Product;
import com.Ishwarjit.Wolf_OVRN_backend.entity.User;
import com.Ishwarjit.Wolf_OVRN_backend.exception.ResourceNotFoundException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.CartItemRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.CartRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.ProductRepository;
import com.Ishwarjit.Wolf_OVRN_backend.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public CartResponse getCart(String userId) {
        UUID uuid = UUID.fromString(userId);
        Cart cart = getOrCreateCart(uuid);
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse addItem(String userId, CartItemRequest request) {
        UUID uuid = UUID.fromString(userId);
        Cart cart = getOrCreateCart(uuid);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found: " + request.getProductId()));

        if (!Boolean.TRUE.equals(product.getIsActive())) {
            throw new ResourceNotFoundException("Product not available");
        }

        String resolvedSize = null;
        if (request.getSizeId() != null) {
            resolvedSize = product.getSizes().stream()
                    .filter(s -> s.getId().equals(request.getSizeId()))
                    .findFirst()
                    .map(com.Ishwarjit.Wolf_OVRN_backend.entity.Size::getSizeName)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid size for product"));
        }

        String resolvedColor = null;
        if (request.getColorId() != null) {
            resolvedColor = product.getColors().stream()
                    .filter(c -> c.getId().equals(request.getColorId()))
                    .findFirst()
                    .map(com.Ishwarjit.Wolf_OVRN_backend.entity.Color::getColorName)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid color for product"));
        }

        List<CartItem> items = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        
        // Final or effectively final variables for lambda
        final String finalSize = resolvedSize;
        final String finalColor = resolvedColor;
        
        Optional<CartItem> matchingItem = items.stream()
                .filter(i -> Objects.equals(i.getSize(), finalSize) && Objects.equals(i.getColor(), finalColor))
                .findFirst();

        matchingItem.ifPresentOrElse(
                existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    cartItemRepository.save(existing);
                },
                () -> {
                    CartItem fresh = new CartItem();
                    fresh.setCart(cart);
                    fresh.setProduct(product);
                    fresh.setQuantity(request.getQuantity());
                    fresh.setSize(finalSize);
                    fresh.setColor(finalColor);
                    CartItem saved = cartItemRepository.save(fresh);
                    cart.getItems().add(saved);
                });

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse updateItem(String userId, UUID cartItemId, UpdateCartItemRequest request) {
        UUID uuid = UUID.fromString(userId);
        Cart cart = getOrCreateCart(uuid);

        CartItem item = cartItemRepository.findById(cartItemId)
                .filter(i -> i.getCart().getId().equals(cart.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        if (request.getQuantity() == 0) {
            cartItemRepository.delete(item);
            cart.getItems().remove(item);
        } else {
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse removeItem(String userId, UUID cartItemId) {
        UUID uuid = UUID.fromString(userId);
        Cart cart = getOrCreateCart(uuid);

        CartItem item = cartItemRepository.findById(cartItemId)
                .filter(i -> i.getCart().getId().equals(cart.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        cartItemRepository.delete(item);
        cart.getItems().remove(item);

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse clearCart(String userId) {
        UUID uuid = UUID.fromString(userId);
        Cart cart = getOrCreateCart(uuid);

        cartItemRepository.deleteByCartId(cart.getId());
        cart.getItems().clear();

        return CartResponse.from(cart);
    }
}
