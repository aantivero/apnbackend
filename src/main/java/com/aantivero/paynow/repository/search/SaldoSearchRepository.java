package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.Saldo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Saldo entity.
 */
public interface SaldoSearchRepository extends ElasticsearchRepository<Saldo, Long> {
}
