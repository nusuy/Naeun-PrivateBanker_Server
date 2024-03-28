package com.naeun.naeun_server.domain.Product.domain;

import com.naeun.naeun_server.domain.Product.dto.ProductSearchItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select new com.naeun.naeun_server.domain.Product.dto.ProductSearchItemDto(p.productId, p.productTitle) " +
            "from Product p " +
            "where p.productTitle like %:query% " +
            "order by p.productTitle asc")
    ArrayList<ProductSearchItemDto> findAllByTitleContainsQueryWithJPQL(String query);
}
