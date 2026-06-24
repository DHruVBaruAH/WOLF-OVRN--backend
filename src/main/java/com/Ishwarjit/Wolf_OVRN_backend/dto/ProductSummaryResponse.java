package com.Ishwarjit.Wolf_OVRN_backend.dto;

import com.Ishwarjit.Wolf_OVRN_backend.entity.Product;
import com.Ishwarjit.Wolf_OVRN_backend.entity.ProductImage;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProductSummaryResponse(
        UUID id,
        String name,
        String slug,
        BigDecimal sellingPrice,
        BigDecimal markedPrice,
        boolean inStock,
        boolean isActive,
        boolean isPremium,
        String primaryImageUrl,
        List<SizeDto> sizes,
        List<ColorDto> colors,
        List<CategoryResponse> categories,
        String description,
        FitDto fit) {

    public static ProductSummaryResponse from(Product product, String primaryImageUrl) {
        List<CategoryResponse> categoryResponses = product.getCategories() == null ? List.of() : 
            product.getCategories().stream().map(CategoryResponse::from).collect(Collectors.toList());
        List<SizeDto> sizeResponses = product.getSizes() == null ? List.of() :
            product.getSizes().stream().map(SizeDto::from).collect(Collectors.toList());
        List<ColorDto> colorResponses = product.getColors() == null ? List.of() :
            product.getColors().stream().map(ColorDto::from).collect(Collectors.toList());
        FitDto fitDto = product.getFit() != null ? FitDto.from(product.getFit()) : null;

        return new ProductSummaryResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getSellingPrice(),
                product.getMarkedPrice(),
                Boolean.TRUE.equals(product.getInStock()),
                Boolean.TRUE.equals(product.getIsActive()),
                Boolean.TRUE.equals(product.getIsPremium()),
                primaryImageUrl,
                sizeResponses,
                colorResponses,
                categoryResponses,
                product.getDescription(),
                fitDto);
    }

    public static ProductSummaryResponse from(Product product, List<ProductImage> images) {
        String url = images.stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .map(ProductImage::getUrl)
                .findFirst()
                .orElseGet(() -> images.isEmpty() ? null : images.get(0).getUrl());
        return from(product, url);
    }
}
