package com.ecommerce.reports.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

public class SalesReport {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalRevenue;
    private Integer totalOrders;
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal averageOrderValue;
    private Integer pendingOrders;
    private Integer completedOrders;

    public SalesReport() {
    }

    public SalesReport(BigDecimal totalRevenue, Integer totalOrders, BigDecimal averageOrderValue,
                       Integer pendingOrders, Integer completedOrders) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.averageOrderValue = averageOrderValue;
        this.pendingOrders = pendingOrders;
        this.completedOrders = completedOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public Integer getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Integer pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Integer getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Integer completedOrders) {
        this.completedOrders = completedOrders;
    }
}
