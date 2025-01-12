package com.example.elk.repository;

import com.example.elk.model.SearchOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderElasticSearchRepository extends ElasticsearchRepository<SearchOrder, String> {
}
