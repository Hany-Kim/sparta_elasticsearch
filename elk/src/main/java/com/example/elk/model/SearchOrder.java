package com.example.elk.model;

import com.example.elk.model.type.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Document(indexName = "order")
public class SearchOrder {
    @Id
    private String id; // ES 에서 자동으로 고유한 ID 를 생성해줍니다.
    private Long orderId;
    private Long totalPrice;
    @Field(name = "product_list") // Field 를 설정하지 않으면 Java 변수명이 자동으로 필드명으로 치환됩니다.
    private List<SearchProduct> products;
    @Field(name = "order_status", type = FieldType.Keyword)
    private OrderStatus status;
    @Field(type = FieldType.Date)
    private LocalDate createdAt;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Builder
    public static class SearchProduct {
        private String name;
        private List<String> images;
        private Long price;
        private Integer quantity;
    }
}
