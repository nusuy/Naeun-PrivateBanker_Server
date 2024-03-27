package com.naeun.naeun_server.domain.Product.dto;

import lombok.Getter;

@Getter
public class ProductSearchItemDto {
    private final Long productId;
    private final String title;

    public ProductSearchItemDto(Long productId, String title) {
        this.productId = productId;
        this.title = title;
    }
}
