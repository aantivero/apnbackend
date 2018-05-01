package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.TransferenciaApp;
import com.aantivero.paynow.repository.TransferenciaAppRepository;
import com.aantivero.paynow.repository.search.TransferenciaAppSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TransferenciaApp.
 */
@Service
@Transactional
public class TransferenciaAppService {

    private final Logger log = LoggerFactory.getLogger(TransferenciaAppService.class);

    private final TransferenciaAppRepository transferenciaAppRepository;

    private final TransferenciaAppSearchRepository transferenciaAppSearchRepository;

    public TransferenciaAppService(TransferenciaAppRepository transferenciaAppRepository, TransferenciaAppSearchRepository transferenciaAppSearchRepository) {
        this.transferenciaAppRepository = transferenciaAppRepository;
        this.transferenciaAppSearchRepository = transferenciaAppSearchRepository;
    }

    /**
     * Save a transferenciaApp.
     *
     * @param transferenciaApp the entity to save
     * @return the persisted entity
     */
    public TransferenciaApp save(TransferenciaApp transferenciaApp) {
        log.debug("Request to save TransferenciaApp : {}", transferenciaApp);
        TransferenciaApp result = transferenciaAppRepository.save(transferenciaApp);
        transferenciaAppSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the transferenciaApps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TransferenciaApp> findAll(Pageable pageable) {
        log.debug("Request to get all TransferenciaApps");
        return transferenciaAppRepository.findAll(pageable);
    }

    /**
     * Get one transferenciaApp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TransferenciaApp findOne(Long id) {
        log.debug("Request to get TransferenciaApp : {}", id);
        return transferenciaAppRepository.findOne(id);
    }

    /**
     * Delete the transferenciaApp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TransferenciaApp : {}", id);
        transferenciaAppRepository.delete(id);
        transferenciaAppSearchRepository.delete(id);
    }

    /**
     * Search for the transferenciaApp corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TransferenciaApp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransferenciaApps for query {}", query);
        Page<TransferenciaApp> result = transferenciaAppSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
