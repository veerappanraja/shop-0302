package com.ecommerce.inventory.service;

import com.ecommerce.config.BusinessRuleException;
import com.ecommerce.config.ResourceNotFoundException;
import com.ecommerce.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public Inventory getInventory(Long productId) {
        return inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
    }

    @Transactional
    public Inventory updateInventory(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        inventory.setQuantity(quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory reserveInventory(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        int available = inventory.getQuantity() - inventory.getReserved();
        if (available < quantity) {
            throw new BusinessRuleException(
                    "Insufficient inventory. Available: " + available + ", Requested: " + quantity
            );
        }

        inventory.setReserved(inventory.getReserved() + quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }
}
