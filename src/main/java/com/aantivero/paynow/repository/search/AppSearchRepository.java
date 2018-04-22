package com.aantivero.paynow.repository.search;

import com.aantivero.paynow.domain.App;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the App entity.
 */
public interface AppSearchRepository extends ElasticsearchRepository<App, Long> {
}
