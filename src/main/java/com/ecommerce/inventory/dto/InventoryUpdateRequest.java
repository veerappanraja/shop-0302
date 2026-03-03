package com.ecommerce.inventory.dto;

public class InventoryUpdateRequest {

    private Integer quantity;

    public InventoryUpdateRequest() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
