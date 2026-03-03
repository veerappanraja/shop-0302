package com.ecommerce.reports.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

public class ProductPerformance {

    private Long productId;
    private String productName;
    private String categoryName;
    private Integer unitsSold;
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal revenue;
    private Integer currentStock;
    private Integer reserved;

    public ProductPerformance() {
    }

    public ProductPerformance(Long productId, String productName, String categoryName,
                              Integer unitsSold, BigDecimal revenue,
                              Integer currentStock, Integer reserved) {
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.unitsSold = unitsSold;
        this.revenue = revenue;
        this.currentStock = currentStock;
        this.reserved = reserved;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(Integer unitsSold) {
        this.unitsSold = unitsSold;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }
}
