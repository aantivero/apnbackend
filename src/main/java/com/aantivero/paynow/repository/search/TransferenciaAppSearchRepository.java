package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.TransferenciaApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransferenciaApp entity.
 */
public interface TransferenciaAppSearchRepository extends ElasticsearchRepository<TransferenciaApp, Long> {
}
