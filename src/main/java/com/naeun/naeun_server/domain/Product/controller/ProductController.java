package com.naeun.naeun_server.domain.Product.controller;

import com.naeun.naeun_server.domain.Product.dto.ProductSearchResDto;
import com.naeun.naeun_server.domain.Product.service.ProductService;
import com.naeun.naeun_server.global.common.DataResponseDto;
import com.naeun.naeun_server.global.common.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/query")
    public ResponseEntity<ResponseDto> searchProduct(@RequestParam(value = "query") @Valid @NotBlank(message = "Invalid query value.") String query) {
        ProductSearchResDto productSearchResDto = productService.searchProduct(query);

        return ResponseEntity.ok(DataResponseDto.of(productSearchResDto, 200));
    }
}
