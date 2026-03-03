package com.ecommerce.orders.service;

import com.ecommerce.config.BusinessRuleException;
import com.ecommerce.config.ResourceNotFoundException;
import com.ecommerce.model.Inventory;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.orders.dto.CreateOrderRequest;
import com.ecommerce.orders.dto.OrderItemRequest;
import com.ecommerce.orders.dto.OrderItemResponse;
import com.ecommerce.orders.dto.OrderResponse;
import com.ecommerce.orders.repository.OrderItemRepository;
import com.ecommerce.orders.repository.OrderRepository;
import com.ecommerce.products.repository.ProductRepository;
import com.ecommerce.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        UserRepository userRepository,
                        ProductRepository productRepository,
                        InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Verify user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String userName = user.getName();

        // Validate products, check inventory, calculate total
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItemData> itemsData = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product " + itemReq.getProductId() + " not found"));

            Inventory inventory = inventoryRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product " + itemReq.getProductId()));

            int available = inventory.getQuantity() - inventory.getReserved();
            if (available < itemReq.getQuantity()) {
                throw new BusinessRuleException(
                        "Insufficient inventory for " + product.getName() + ". Available: " + available);
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            total = total.add(itemTotal);

            itemsData.add(new OrderItemData(product, inventory, itemReq.getQuantity()));
        }

        // Create order
        Order order = new Order(request.getUserId(), "pending", total);
        order = orderRepository.save(order);

        // Create order items and reserve inventory
        List<OrderItemResponse> itemsResponse = new ArrayList<>();
        for (OrderItemData itemData : itemsData) {
            OrderItem orderItem = new OrderItem(
                    order.getId(),
                    itemData.product.getId(),
                    itemData.quantity,
                    itemData.product.getPrice()
            );
            orderItemRepository.save(orderItem);

            // Reserve inventory
            Inventory inventory = itemData.inventory;
            inventory.setReserved(inventory.getReserved() + itemData.quantity);
            inventory.setLastUpdated(LocalDateTime.now());
            inventoryRepository.save(inventory);

            itemsResponse.add(new OrderItemResponse(
                    itemData.product.getId(),
                    itemData.product.getName(),
                    itemData.quantity,
                    itemData.product.getPrice().doubleValue()
            ));
        }

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                userName,
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                itemsResponse
        );
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        List<OrderItemResponse> itemsResponse = new ArrayList<>();
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                itemsResponse.add(new OrderItemResponse(
                        product.getId(),
                        product.getName(),
                        item.getQuantity(),
                        item.getPrice().doubleValue()
                ));
            }
        }

        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                user.getName(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                itemsResponse
        );
    }

    @Transactional(readOnly = true)
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    private static class OrderItemData {
        final Product product;
        final Inventory inventory;
        final int quantity;

        OrderItemData(Product product, Inventory inventory, int quantity) {
            this.product = product;
            this.inventory = inventory;
            this.quantity = quantity;
        }
    }
}
