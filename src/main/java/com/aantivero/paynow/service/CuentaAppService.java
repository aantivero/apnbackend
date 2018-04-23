package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.CuentaApp;
import com.aantivero.paynow.domain.SaldoApp;
import com.aantivero.paynow.repository.CuentaAppRepository;
import com.aantivero.paynow.repository.search.CuentaAppSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CuentaApp.
 */
@Service
@Transactional
public class CuentaAppService {

    private final Logger log = LoggerFactory.getLogger(CuentaAppService.class);

    private final CuentaAppRepository cuentaAppRepository;

    private final CuentaAppSearchRepository cuentaAppSearchRepository;

    private final SaldoAppService saldoAppService;

    public CuentaAppService(CuentaAppRepository cuentaAppRepository, CuentaAppSearchRepository cuentaAppSearchRepository,
                            SaldoAppService saldoAppService) {
        this.cuentaAppRepository = cuentaAppRepository;
        this.cuentaAppSearchRepository = cuentaAppSearchRepository;
        this.saldoAppService = saldoAppService;
    }

    /**
     * Save a cuentaApp.
     *
     * @param cuentaApp the entity to save
     * @return the persisted entity
     */
    public CuentaApp save(CuentaApp cuentaApp) {
        log.debug("Request to save CuentaApp : {}", cuentaApp);
        CuentaApp result = cuentaAppRepository.save(cuentaApp);
        cuentaAppSearchRepository.save(result);
        log.debug("Chequear que existe saldo creado para la aplicacion y moneda");
        SaldoApp saldoApp = saldoAppService.searchByAppAndMoneda(cuentaApp.getApp(), cuentaApp.getMoneda());
        if (saldoApp == null) {
            saldoApp = new SaldoApp();
            saldoApp.setApp(cuentaApp.getApp());
            saldoApp.setMoneda(cuentaApp.getMoneda());
            saldoApp.setMonto(cuentaApp.getSaldo());
        } else {
            saldoApp.setMonto(saldoApp.getMonto().add(cuentaApp.getSaldo()));
        }
        saldoAppService.save(saldoApp);
        return result;
    }

    /**
     * TODO desarrollar esta parte
     * Actualizar la cuentaApp teniendo en cuenta el saldo anterior para impactar en el saldo de la App
     * @param cuentaApp
     * @return
     */
    public CuentaApp update(CuentaApp cuentaApp) {
        log.debug("Obtener el saldo anterior de la cuenta");
        CuentaApp anterior = cuentaAppRepository.findOne(cuentaApp.getId());
        return null;
    }

    /**
     * Get all the cuentaApps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CuentaApp> findAll(Pageable pageable) {
        log.debug("Request to get all CuentaApps");
        return cuentaAppRepository.findAll(pageable);
    }

    /**
     * Get one cuentaApp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CuentaApp findOne(Long id) {
        log.debug("Request to get CuentaApp : {}", id);
        return cuentaAppRepository.findOne(id);
    }

    /**
     * Delete the cuentaApp by id.
     * también elimina el saldo de esa cuenta del saldo general
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CuentaApp : {}", id);
        //eliminar el saldo correspondiente
        CuentaApp cuentaApp = findOne(id);
        if (cuentaApp != null) {
            SaldoApp saldoApp = saldoAppService.searchByAppAndMoneda(cuentaApp.getApp(), cuentaApp.getMoneda());
            if (saldoApp != null) {
                saldoApp.setMonto(saldoApp.getMonto().subtract(cuentaApp.getSaldo()));
                saldoAppService.save(saldoApp);
            }
        }
        cuentaAppRepository.delete(id);
        cuentaAppSearchRepository.delete(id);
    }

    /**
     * Search for the cuentaApp corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CuentaApp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CuentaApps for query {}", query);
        Page<CuentaApp> result = cuentaAppSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
