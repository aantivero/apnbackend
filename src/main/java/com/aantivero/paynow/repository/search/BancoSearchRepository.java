package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.Banco;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Banco entity.
 */
public interface BancoSearchRepository extends ElasticsearchRepository<Banco, Long> {
}
