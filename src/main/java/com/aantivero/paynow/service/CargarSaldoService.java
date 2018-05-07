package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.CargarSaldo;
import com.aantivero.paynow.repository.CargarSaldoRepository;
import com.aantivero.paynow.repository.search.CargarSaldoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CargarSaldo.
 */
@Service
@Transactional
public class CargarSaldoService {

    private final Logger log = LoggerFactory.getLogger(CargarSaldoService.class);

    private final CargarSaldoRepository cargarSaldoRepository;

    private final CargarSaldoSearchRepository cargarSaldoSearchRepository;

    public CargarSaldoService(CargarSaldoRepository cargarSaldoRepository, CargarSaldoSearchRepository cargarSaldoSearchRepository) {
        this.cargarSaldoRepository = cargarSaldoRepository;
        this.cargarSaldoSearchRepository = cargarSaldoSearchRepository;
    }

    /**
     * Save a cargarSaldo.
     *
     * @param cargarSaldo the entity to save
     * @return the persisted entity
     */
    public CargarSaldo save(CargarSaldo cargarSaldo) {
        log.debug("Request to save CargarSaldo : {}", cargarSaldo);
        CargarSaldo result = cargarSaldoRepository.save(cargarSaldo);
        cargarSaldoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cargarSaldos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CargarSaldo> findAll(Pageable pageable) {
        log.debug("Request to get all CargarSaldos");
        return cargarSaldoRepository.findAll(pageable);
    }

    /**
     * Get one cargarSaldo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CargarSaldo findOne(Long id) {
        log.debug("Request to get CargarSaldo : {}", id);
        return cargarSaldoRepository.findOne(id);
    }

    /**
     * Delete the cargarSaldo by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CargarSaldo : {}", id);
        cargarSaldoRepository.delete(id);
        cargarSaldoSearchRepository.delete(id);
    }

    /**
     * Search for the cargarSaldo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CargarSaldo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CargarSaldos for query {}", query);
        Page<CargarSaldo> result = cargarSaldoSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
