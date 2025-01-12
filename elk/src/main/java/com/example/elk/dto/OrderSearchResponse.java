package com.example.elk.dto;

import com.example.elk.model.type.OrderStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderSearchResponse {
    Long orderId;
    Long totalPrice;
    List<ProductResponse> products;
    OrderStatus status;
    LocalDate createdAt;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductResponse {
        private String name;
        private List<String> images;
        private Long price;
        private Integer quantity;
    }
}
