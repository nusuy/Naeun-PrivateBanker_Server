package com.naeun.naeun_server.domain.Product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class ProductSearchResDto {
    private ArrayList<ProductSearchItemDto> result;
}
