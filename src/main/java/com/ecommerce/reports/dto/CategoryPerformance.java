package com.ecommerce.reports.dto;

import java.math.BigDecimal;

public class CategoryPerformance {

    private Long categoryId;
    private String categoryName;
    private Integer productCount;
    private BigDecimal totalRevenue;
    private Integer unitsSold;

    public CategoryPerformance() {
    }

    public CategoryPerformance(Long categoryId, String categoryName, Integer productCount,
                               BigDecimal totalRevenue, Integer unitsSold) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productCount = productCount;
        this.totalRevenue = totalRevenue;
        this.unitsSold = unitsSold;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(Integer unitsSold) {
        this.unitsSold = unitsSold;
    }
}
