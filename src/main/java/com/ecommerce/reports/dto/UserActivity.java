package com.ecommerce.reports.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

public class UserActivity {

    private Long userId;
    private String userName;
    private String userEmail;
    private Integer totalOrders;
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalSpent;

    public UserActivity() {
    }

    public UserActivity(Long userId, String userName, String userEmail,
                        Integer totalOrders, BigDecimal totalSpent) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
}
