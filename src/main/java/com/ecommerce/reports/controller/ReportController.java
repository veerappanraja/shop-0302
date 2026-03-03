package com.ecommerce.reports.controller;

import com.ecommerce.reports.dto.CategoryPerformance;
import com.ecommerce.reports.dto.InventoryReportDto;
import com.ecommerce.reports.dto.ProductPerformance;
import com.ecommerce.reports.dto.SalesReport;
import com.ecommerce.reports.dto.UserActivity;
import com.ecommerce.reports.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales")
    public SalesReport getSalesReport() {
        return reportService.getSalesReport();
    }

    @GetMapping("/inventory")
    public InventoryReportDto getInventoryReport(
            @RequestParam(value = "low_stock_threshold", defaultValue = "10") int lowStockThreshold) {
        return reportService.getInventoryReport(lowStockThreshold);
    }

    @GetMapping("/products")
    public List<ProductPerformance> getProductPerformance() {
        return reportService.getProductPerformance();
    }

    @GetMapping("/categories")
    public List<CategoryPerformance> getCategoryPerformance() {
        return reportService.getCategoryPerformance();
    }

    @GetMapping("/users")
    public List<UserActivity> getUserActivity() {
        return reportService.getUserActivity();
    }
}
