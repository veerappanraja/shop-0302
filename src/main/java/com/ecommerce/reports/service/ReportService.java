package com.ecommerce.reports.service;

import com.ecommerce.model.Category;
import com.ecommerce.model.Inventory;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.orders.repository.OrderItemRepository;
import com.ecommerce.orders.repository.OrderRepository;
import com.ecommerce.products.repository.CategoryRepository;
import com.ecommerce.products.repository.ProductRepository;
import com.ecommerce.users.repository.UserRepository;
import com.ecommerce.reports.dto.CategoryPerformance;
import com.ecommerce.reports.dto.InventoryReportDto;
import com.ecommerce.reports.dto.ProductPerformance;
import com.ecommerce.reports.dto.SalesReport;
import com.ecommerce.reports.dto.UserActivity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    public ReportService(OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository,
                         ProductRepository productRepository,
                         CategoryRepository categoryRepository,
                         InventoryRepository inventoryRepository,
                         UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public SalesReport getSalesReport() {
        List<Order> orders = orderRepository.findAll();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalOrders = orders.size();
        int pendingOrders = 0;
        int completedOrders = 0;

        for (Order order : orders) {
            if (order.getTotal() != null) {
                totalRevenue = totalRevenue.add(order.getTotal());
            }
            if ("pending".equals(order.getStatus())) {
                pendingOrders++;
            }
            if ("completed".equals(order.getStatus())) {
                completedOrders++;
            }
        }

        BigDecimal averageOrderValue = BigDecimal.ZERO;
        if (totalOrders > 0 && totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            averageOrderValue = totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);
        }

        return new SalesReport(totalRevenue, totalOrders, averageOrderValue, pendingOrders, completedOrders);
    }

    @Transactional(readOnly = true)
    public InventoryReportDto getInventoryReport(int lowStockThreshold) {
        List<Product> products = productRepository.findAll();
        List<Inventory> inventories = inventoryRepository.findAll();

        int totalProducts = products.size();
        int totalStock = 0;
        int totalReserved = 0;
        int lowStockProducts = 0;

        for (Inventory inv : inventories) {
            totalStock += inv.getQuantity();
            totalReserved += inv.getReserved();
            int available = inv.getQuantity() - inv.getReserved();
            if (available < lowStockThreshold) {
                lowStockProducts++;
            }
        }

        int availableStock = totalStock - totalReserved;

        return new InventoryReportDto(totalProducts, totalStock, totalReserved, availableStock, lowStockProducts);
    }

    @Transactional(readOnly = true)
    public List<ProductPerformance> getProductPerformance() {
        List<Product> products = productRepository.findAll();
        List<OrderItem> allOrderItems = orderItemRepository.findAll();
        List<Inventory> allInventories = inventoryRepository.findAll();

        List<ProductPerformance> performance = new ArrayList<>();

        for (Product product : products) {
            // Only include products that have a category (the Python code uses inner join)
            if (product.getCategoryId() == null) {
                continue;
            }

            Optional<Category> categoryOpt = categoryRepository.findById(product.getCategoryId());
            if (categoryOpt.isEmpty()) {
                continue;
            }
            Category category = categoryOpt.get();

            // Calculate units sold and revenue from order items
            int unitsSold = 0;
            BigDecimal revenue = BigDecimal.ZERO;
            for (OrderItem item : allOrderItems) {
                if (item.getProductId().equals(product.getId())) {
                    unitsSold += item.getQuantity();
                    revenue = revenue.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }
            }

            // Get inventory
            int currentStock = 0;
            int reserved = 0;
            for (Inventory inv : allInventories) {
                if (inv.getProductId().equals(product.getId())) {
                    currentStock = inv.getQuantity();
                    reserved = inv.getReserved();
                    break;
                }
            }

            performance.add(new ProductPerformance(
                    product.getId(),
                    product.getName(),
                    category.getName(),
                    unitsSold,
                    revenue,
                    currentStock,
                    reserved
            ));
        }

        // Sort by revenue descending
        performance.sort((a, b) -> b.getRevenue().compareTo(a.getRevenue()));
        return performance;
    }

    @Transactional(readOnly = true)
    public List<CategoryPerformance> getCategoryPerformance() {
        List<Category> categories = categoryRepository.findAll();
        List<Product> allProducts = productRepository.findAll();
        List<OrderItem> allOrderItems = orderItemRepository.findAll();

        List<CategoryPerformance> performance = new ArrayList<>();

        for (Category category : categories) {
            // Count products in this category
            int productCount = 0;
            List<Long> productIds = new ArrayList<>();
            for (Product product : allProducts) {
                if (category.getId().equals(product.getCategoryId())) {
                    productCount++;
                    productIds.add(product.getId());
                }
            }

            // Calculate units sold and revenue
            int unitsSold = 0;
            BigDecimal totalRevenue = BigDecimal.ZERO;
            for (OrderItem item : allOrderItems) {
                if (productIds.contains(item.getProductId())) {
                    unitsSold += item.getQuantity();
                    totalRevenue = totalRevenue.add(
                            item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }
            }

            performance.add(new CategoryPerformance(
                    category.getId(),
                    category.getName(),
                    productCount,
                    totalRevenue,
                    unitsSold
            ));
        }

        // Sort by revenue descending
        performance.sort((a, b) -> b.getTotalRevenue().compareTo(a.getTotalRevenue()));
        return performance;
    }

    @Transactional(readOnly = true)
    public List<UserActivity> getUserActivity() {
        List<User> users = userRepository.findAll();
        List<Order> allOrders = orderRepository.findAll();

        List<UserActivity> activity = new ArrayList<>();

        for (User user : users) {
            int totalOrders = 0;
            BigDecimal totalSpent = BigDecimal.ZERO;

            for (Order order : allOrders) {
                if (user.getId().equals(order.getUserId())) {
                    totalOrders++;
                    if (order.getTotal() != null) {
                        totalSpent = totalSpent.add(order.getTotal());
                    }
                }
            }

            activity.add(new UserActivity(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    totalOrders,
                    totalSpent
            ));
        }

        // Sort by total spent descending
        activity.sort((a, b) -> b.getTotalSpent().compareTo(a.getTotalSpent()));
        return activity;
    }
}
