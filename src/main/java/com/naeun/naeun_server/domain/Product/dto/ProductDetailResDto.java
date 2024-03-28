package com.naeun.naeun_server.domain.Product.dto;

import com.naeun.naeun_server.domain.Product.domain.Product;
import com.naeun.naeun_server.global.util.StringUtil;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ProductDetailResDto {
    private Long productId;
    private String title;
    private String seller;
    private String type;
    private Integer risk;
    private Float earningsRate;
    private Integer lossRate;
    private ArrayList<String> underlying;
    private String desc;

    public ProductDetailResDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getProductTitle();
        this.seller = product.getProductSeller();
        this.type = product.getProductType();
        this.risk = product.getProductRisk();
        this.earningsRate = product.getProductEarningsRate();
        this.lossRate = product.getProductLossRate();
        this.underlying = StringUtil.getUnderlying(product.getProductUnderlying());
        this.desc = product.getProductDesc();
    }
}
