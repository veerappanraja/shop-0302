package com.ecommerce.inventory.controller;

import com.ecommerce.model.Inventory;
import com.ecommerce.inventory.dto.InventoryUpdateRequest;
import com.ecommerce.inventory.dto.ReserveRequest;
import com.ecommerce.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{product_id}")
    public Inventory getInventory(@PathVariable("product_id") Long productId) {
        return inventoryService.getInventory(productId);
    }

    @PutMapping("/{product_id}")
    public Inventory updateInventory(@PathVariable("product_id") Long productId,
                                     @RequestBody InventoryUpdateRequest request) {
        return inventoryService.updateInventory(productId, request.getQuantity());
    }

    @PostMapping("/{product_id}/reserve")
    public Inventory reserveInventory(@PathVariable("product_id") Long productId,
                                      @RequestBody ReserveRequest request) {
        return inventoryService.reserveInventory(productId, request.getQuantity());
    }
}
