package com.example.elk.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.example.elk.dto.OrderCreateRequest;
import com.example.elk.dto.OrderSearchResponse;
import com.example.elk.model.SearchOrder;
import com.example.elk.model.type.OrderStatus;
import com.example.elk.repository.OrderElasticSearchRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
public class SearchOrderServiceImpl {
    private final ElasticsearchOperations elasticsearchOperations;
    private final OrderElasticSearchRepository orderElasticSearchRepository; // 추가됨

    public SearchOrderServiceImpl(ElasticsearchOperations elasticsearchOperations,
                                  OrderElasticSearchRepository orderElasticSearchRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.orderElasticSearchRepository = orderElasticSearchRepository;
    }

    public List<OrderSearchResponse> search(String productName, Long startPrice, Long endPrice) {
        NativeQueryBuilder query = NativeQuery.builder();
        BoolQuery.Builder boolQuery = QueryBuilders.bool();

        // Range 조건 추가
        if (startPrice != null && endPrice != null) {
            Query priceQuery = QueryBuilders.range(builder ->
                    builder.number(numberBuilder -> numberBuilder.field("totalPrice")
                            .gte(startPrice.doubleValue()) // greater than or equal to
                            .lte(endPrice.doubleValue()))); // less than or equal to
            boolQuery.must(priceQuery);
        }

        // 문자열 포함 쿼리
        boolQuery.must(QueryBuilders.queryString(field ->
                field.fields(List.of("product_list.name")).query("*%s*".formatted(productName))));
        // sql = %{쿼리}% -> '쿼리' 포함된 것조회

        query.withQuery(boolQuery.build()._toQuery());
        SearchHits<SearchOrder> hits = elasticsearchOperations.search(query.build(), SearchOrder.class);
        return hits.map(hit -> toDto(hit.getContent())).toList();
    }

    private static OrderSearchResponse toDto(SearchOrder order) {
        return OrderSearchResponse.builder()
                .orderId(order.getOrderId())
                .totalPrice(order.getTotalPrice())
                .products(order.getProducts().stream()
                        .map(SearchOrderServiceImpl::toDto)
                        .toList()
                )
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private static OrderSearchResponse.ProductResponse toDto(SearchOrder.SearchProduct product) {
        return OrderSearchResponse.ProductResponse.builder()
                .name(product.getName())
                .images(product.getImages())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public void create(OrderCreateRequest request) {
        orderElasticSearchRepository.save(requestToSearchOrder(request));
    }

    public SearchOrder requestToSearchOrder(OrderCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return SearchOrder.builder()
                .orderId((long) RandomUtil.getPositiveInt())
                .totalPrice(request.getProducts().stream().mapToLong(OrderCreateRequest.ProductCreateRequest::getPrice).sum())
                .products(request.getProducts().stream()
                        .map(product -> SearchOrder.SearchProduct.builder()
                                .name(product.getName())
                                .price(product.getPrice())
                                .images(product.getImages())
                                .quantity(product.getQuantity())
                                .build()).toList())
                .status(OrderStatus.LOADING_PRODUCT)
                .createdAt(now.toLocalDate())
                .build();
    }
}
