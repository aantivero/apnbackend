package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.domain.SaldoApp;
import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.repository.SaldoAppRepository;
import com.aantivero.paynow.repository.search.SaldoAppSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SaldoApp.
 */
@Service
@Transactional
public class SaldoAppService {

    private final Logger log = LoggerFactory.getLogger(SaldoAppService.class);

    private final SaldoAppRepository saldoAppRepository;

    private final SaldoAppSearchRepository saldoAppSearchRepository;

    public SaldoAppService(SaldoAppRepository saldoAppRepository, SaldoAppSearchRepository saldoAppSearchRepository) {
        this.saldoAppRepository = saldoAppRepository;
        this.saldoAppSearchRepository = saldoAppSearchRepository;
    }

    /**
     * Save a saldoApp.
     *
     * @param saldoApp the entity to save
     * @return the persisted entity
     */
    public SaldoApp save(SaldoApp saldoApp) {
        log.debug("Request to save SaldoApp : {}", saldoApp);
        SaldoApp result = saldoAppRepository.save(saldoApp);
        saldoAppSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the saldoApps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SaldoApp> findAll(Pageable pageable) {
        log.debug("Request to get all SaldoApps");
        return saldoAppRepository.findAll(pageable);
    }

    /**
     * Get one saldoApp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SaldoApp findOne(Long id) {
        log.debug("Request to get SaldoApp : {}", id);
        return saldoAppRepository.findOne(id);
    }

    /**
     * Delete the saldoApp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SaldoApp : {}", id);
        saldoAppRepository.delete(id);
        saldoAppSearchRepository.delete(id);
    }

    /**
     * Search for the saldoApp corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SaldoApp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SaldoApps for query {}", query);
        Page<SaldoApp> result = saldoAppSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public SaldoApp searchByAppAndMoneda(App app, Moneda moneda) {
        log.debug("Buscar Saldo por Moneda y App");
        SaldoApp result = saldoAppRepository.findByAppAndMoneda(app, moneda);
        return result;
    }

}
