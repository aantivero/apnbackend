package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.CuentaApp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CuentaApp entity.
 */
public interface CuentaAppSearchRepository extends ElasticsearchRepository<CuentaApp, Long> {
}
