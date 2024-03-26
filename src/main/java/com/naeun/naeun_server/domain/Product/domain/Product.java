package com.naeun.naeun_server.domain.Product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ProductData")
@Getter
@NoArgsConstructor
@EntityListeners(EnableJpaAuditing.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productTitle;

    private String productSeller;

    private String productIssuer;

    private String productType;

    private Integer productRisk;

    private Float productEarningsRate;

    private Integer productLossRate;

    private String productUnderlying;

    private Integer productExpPeriod;

    @Column(columnDefinition = "TEXT")
    private String productDesc;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product product)) return false;

        return Objects.equals(this.productId, product.getProductId()) &&
                Objects.equals(this.productTitle, product.getProductTitle()) &&
                Objects.equals(this.productSeller, product.getProductSeller()) &&
                Objects.equals(this.productType, product.getProductType()) &&
                Objects.equals(this.productEarningsRate, product.getProductEarningsRate()) &&
                Objects.equals(this.productLossRate, product.getProductLossRate()) &&
                Objects.equals(this.productExpPeriod, product.getProductExpPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productTitle, productSeller, productType, productEarningsRate, productLossRate, productExpPeriod);
    }
}
