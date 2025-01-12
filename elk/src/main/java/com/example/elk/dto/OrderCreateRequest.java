package com.example.elk.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    List<ProductCreateRequest> products;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductCreateRequest {
        String name;
        Long price;
        Integer quantity;
        List<String> images;
    }
}