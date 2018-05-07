package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.CargarSaldo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CargarSaldo entity.
 */
public interface CargarSaldoSearchRepository extends ElasticsearchRepository<CargarSaldo, Long> {
}
