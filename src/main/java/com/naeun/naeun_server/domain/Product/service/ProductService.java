package com.naeun.naeun_server.domain.Product.service;

import com.naeun.naeun_server.domain.Product.domain.Product;
import com.naeun.naeun_server.domain.Product.domain.ProductRepository;
import com.naeun.naeun_server.domain.Product.dto.ProductDetailResDto;
import com.naeun.naeun_server.domain.Product.dto.ProductSearchItemDto;
import com.naeun.naeun_server.domain.Product.dto.ProductSearchResDto;
import com.naeun.naeun_server.domain.Product.error.ProductErrorCode;
import com.naeun.naeun_server.global.error.exception.AppException;
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

    // Product detail
    @Transactional(readOnly = true)
    public ProductDetailResDto readProductDetail(Long productId) {
        // Find DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return new ProductDetailResDto(product);
    }
}
