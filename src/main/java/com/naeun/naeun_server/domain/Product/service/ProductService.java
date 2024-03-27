package com.naeun.naeun_server.domain.Product.service;

import com.naeun.naeun_server.domain.Product.domain.ProductRepository;
import com.naeun.naeun_server.domain.Product.dto.ProductSearchItemDto;
import com.naeun.naeun_server.domain.Product.dto.ProductSearchResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // Search product
    @Transactional(readOnly = true)
    public ProductSearchResDto searchProduct(String query) {
        ArrayList<ProductSearchItemDto> result = productRepository.findAllByTitleContainsQueryWithJPQL(query);

        return new ProductSearchResDto(result);
    }
}
