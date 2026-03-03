package com.ecommerce.reports.dto;

public class InventoryReportDto {

    private Integer totalProducts;
    private Integer totalStock;
    private Integer totalReserved;
    private Integer availableStock;
    private Integer lowStockProducts;

    public InventoryReportDto() {
    }

    public InventoryReportDto(Integer totalProducts, Integer totalStock, Integer totalReserved,
                              Integer availableStock, Integer lowStockProducts) {
        this.totalProducts = totalProducts;
        this.totalStock = totalStock;
        this.totalReserved = totalReserved;
        this.availableStock = availableStock;
        this.lowStockProducts = lowStockProducts;
    }

    public Integer getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Integer totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getTotalReserved() {
        return totalReserved;
    }

    public void setTotalReserved(Integer totalReserved) {
        this.totalReserved = totalReserved;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Integer getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(Integer lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }
}
