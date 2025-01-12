package com.example.elk.controller;

import com.example.elk.dto.OrderCreateRequest;
import com.example.elk.dto.OrderSearchResponse;
import com.example.elk.service.SearchOrderServiceImpl;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final SearchOrderServiceImpl searchOrderService;

    public OrderController(SearchOrderServiceImpl searchOrderService) {
        this.searchOrderService = searchOrderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderSearchResponse>> getOrders(
            @RequestParam(value = "product") String productName,
            @RequestParam(value = "start_price", required = false) Long startPrice,
            @RequestParam(value = "end_price", required = false) Long endPrice
    ) {
        List<OrderSearchResponse> response = searchOrderService.search(productName, startPrice, endPrice);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Boolean> createOrder(
            @RequestBody OrderCreateRequest request
    ) {
        searchOrderService.create(request);
        return ResponseEntity.ok(true);
    }
}
