package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.CuentaApp;
import com.aantivero.paynow.domain.MovimientoApp;
import com.aantivero.paynow.repository.MovimientoAppRepository;
import com.aantivero.paynow.repository.search.MovimientoAppSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.Instant;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MovimientoApp.
 */
@Service
@Transactional
public class MovimientoAppService {

    private final Logger log = LoggerFactory.getLogger(MovimientoAppService.class);

    private final MovimientoAppRepository movimientoAppRepository;

    private final MovimientoAppSearchRepository movimientoAppSearchRepository;

    private final CuentaAppService cuentaAppService;

    public MovimientoAppService(MovimientoAppRepository movimientoAppRepository,
                                MovimientoAppSearchRepository movimientoAppSearchRepository,
                                CuentaAppService cuentaAppService) {
        this.movimientoAppRepository = movimientoAppRepository;
        this.movimientoAppSearchRepository = movimientoAppSearchRepository;
        this.cuentaAppService = cuentaAppService;
    }

    /**
     * Save a movimientoApp.
     *
     * @param movimientoApp the entity to save
     * @return the persisted entity
     */
    public MovimientoApp save(MovimientoApp movimientoApp) {
        //TODO falta considerar si el movimiento ya no fue consolidado
        log.debug("Request guardar MovimientoApp : {}", movimientoApp);
        //buscar y actualizar la cuentaApp
        boolean consolidadoDebito = false;
        if (movimientoApp.isCuentaDebitoPropia()) {
            CuentaApp cuentaApp = getCuentaApp(movimientoApp.getCuentaDebitoAlias(),
                movimientoApp.getCuentaDebitoCbu());
            if(cuentaApp != null) {
                log.debug("Debito Encontrada:" + cuentaApp);
                BigDecimal nuevoSaldo = cuentaApp.getSaldo().subtract(movimientoApp.getMonto());
                cuentaAppService.update(cuentaApp, nuevoSaldo);
                consolidadoDebito = true;
            } else {
                log.debug("No se encontro cuentaApp debito");
                //TODO pensar y ver que hacer si la cuenta app no existe en la aplicación
            }
        } else {
            log.debug("La cuenta debito no es propia");
        }
        boolean consolidadoCredito = false;
        if (movimientoApp.isCuentaCreditoPropia()) {
            CuentaApp cuentaApp = getCuentaApp(movimientoApp.getCuentaCreditoAlias(),
                movimientoApp.getCuentaCreditoCbu());
            if(cuentaApp != null){
                log.debug("Credito Encontrada:"+cuentaApp);
                BigDecimal nuevoSaldo = cuentaApp.getSaldo().add(movimientoApp.getMonto());
                cuentaAppService.update(cuentaApp, nuevoSaldo);
                consolidadoCredito = true;
            } else {
                log.debug("No se encontro la cuentaApp credito");
            }
        } else {
            log.debug("la cuenta credito no es propia");
        }
        if (consolidadoDebito || consolidadoCredito){
            movimientoApp.setConsolidado(true);
            Instant instant = Instant.now();
            movimientoApp.setConsolidadoTimestamp(instant);
        }
        MovimientoApp result = movimientoAppRepository.save(movimientoApp);
        movimientoAppSearchRepository.save(result);
        return result;
    }

    @Transactional(readOnly = true)
    public CuentaApp getCuentaApp(String aliasCbu, String cbu){
        final CuentaApp cuentaApp = cuentaAppService.findByAliasCbuOrCbu(aliasCbu, cbu);
        return cuentaApp;
    }

    /**
     * Get all the movimientoApps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MovimientoApp> findAll(Pageable pageable) {
        log.debug("Request to get all MovimientoApps");
        return movimientoAppRepository.findAll(pageable);
    }

    /**
     * Get one movimientoApp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MovimientoApp findOne(Long id) {
        log.debug("Request to get MovimientoApp : {}", id);
        return movimientoAppRepository.findOne(id);
    }

    /**
     * Delete the movimientoApp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MovimientoApp : {}", id);
        movimientoAppRepository.delete(id);
        movimientoAppSearchRepository.delete(id);
    }

    /**
     * Search for the movimientoApp corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MovimientoApp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MovimientoApps for query {}", query);
        Page<MovimientoApp> result = movimientoAppSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
