package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.SaldoApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SaldoApp entity.
 */
public interface SaldoAppSearchRepository extends ElasticsearchRepository<SaldoApp, Long> {
}
