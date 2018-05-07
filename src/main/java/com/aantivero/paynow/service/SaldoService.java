package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.Saldo;
import com.aantivero.paynow.repository.SaldoRepository;
import com.aantivero.paynow.repository.search.SaldoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Saldo.
 */
@Service
@Transactional
public class SaldoService {

    private final Logger log = LoggerFactory.getLogger(SaldoService.class);

    private final SaldoRepository saldoRepository;

    private final SaldoSearchRepository saldoSearchRepository;

    public SaldoService(SaldoRepository saldoRepository, SaldoSearchRepository saldoSearchRepository) {
        this.saldoRepository = saldoRepository;
        this.saldoSearchRepository = saldoSearchRepository;
    }

    /**
     * Save a saldo.
     *
     * @param saldo the entity to save
     * @return the persisted entity
     */
    public Saldo save(Saldo saldo) {
        log.debug("Request to save Saldo : {}", saldo);
        Saldo result = saldoRepository.save(saldo);
        saldoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the saldos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Saldo> findAll(Pageable pageable) {
        log.debug("Request to get all Saldos");
        return saldoRepository.findAll(pageable);
    }

    /**
     * Get one saldo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Saldo findOne(Long id) {
        log.debug("Request to get Saldo : {}", id);
        return saldoRepository.findOne(id);
    }

    /**
     * Delete the saldo by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Saldo : {}", id);
        saldoRepository.delete(id);
        saldoSearchRepository.delete(id);
    }

    /**
     * Search for the saldo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Saldo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Saldos for query {}", query);
        Page<Saldo> result = saldoSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
