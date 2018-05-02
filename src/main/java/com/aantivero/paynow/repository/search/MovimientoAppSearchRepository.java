package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.MovimientoApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MovimientoApp entity.
 */
public interface MovimientoAppSearchRepository extends ElasticsearchRepository<MovimientoApp, Long> {
}
