package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.Cuenta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cuenta entity.
 */
public interface CuentaSearchRepository extends ElasticsearchRepository<Cuenta, Long> {
}
