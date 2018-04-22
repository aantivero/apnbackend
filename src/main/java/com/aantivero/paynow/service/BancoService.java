package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.Banco;
import com.aantivero.paynow.repository.BancoRepository;
import com.aantivero.paynow.repository.search.BancoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Banco.
 */
@Service
@Transactional
public class BancoService {

    private final Logger log = LoggerFactory.getLogger(BancoService.class);

    private final BancoRepository bancoRepository;

    private final BancoSearchRepository bancoSearchRepository;

    public BancoService(BancoRepository bancoRepository, BancoSearchRepository bancoSearchRepository) {
        this.bancoRepository = bancoRepository;
        this.bancoSearchRepository = bancoSearchRepository;
    }

    /**
     * Save a banco.
     *
     * @param banco the entity to save
     * @return the persisted entity
     */
    public Banco save(Banco banco) {
        log.debug("Request to save Banco : {}", banco);
        Banco result = bancoRepository.save(banco);
        bancoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the bancos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Banco> findAll(Pageable pageable) {
        log.debug("Request to get all Bancos");
        return bancoRepository.findAll(pageable);
    }

    /**
     * Get one banco by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Banco findOne(Long id) {
        log.debug("Request to get Banco : {}", id);
        return bancoRepository.findOne(id);
    }

    /**
     * Delete the banco by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Banco : {}", id);
        bancoRepository.delete(id);
        bancoSearchRepository.delete(id);
    }

    /**
     * Search for the banco corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Banco> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bancos for query {}", query);
        Page<Banco> result = bancoSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
