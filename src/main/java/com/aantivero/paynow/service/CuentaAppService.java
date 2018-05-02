package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.domain.CuentaApp;
import com.aantivero.paynow.domain.SaldoApp;
import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.repository.CuentaAppRepository;
import com.aantivero.paynow.repository.search.CuentaAppSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;

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
        actualizarSaldoApp(cuentaApp.getApp(), cuentaApp.getMoneda(), cuentaApp.getSaldo(), true);
        return result;
    }

    private void actualizarSaldoApp(App app, Moneda moneda, BigDecimal saldo, boolean add) {
        SaldoApp saldoApp = saldoAppService.searchByAppAndMoneda(app, moneda);
        if (saldoApp == null) {
            saldoApp = new SaldoApp();
            saldoApp.setApp(app);
            saldoApp.setMoneda(moneda);
            saldoApp.setMonto(saldo);
        } else {
            if (add) {
                saldoApp.setMonto(saldoApp.getMonto().add(saldo));
            } else {
                saldoApp.setMonto(saldoApp.getMonto().subtract(saldo));
            }
        }
        saldoAppService.save(saldoApp);
    }

    /**
     * Actualizar la cuentaApp teniendo en cuenta el saldo anterior para impactar en el saldo de la App
     * @param cuentaApp
     * @return
     */
    public CuentaApp update(CuentaApp cuentaApp) {
        log.debug("Modificacion de la cuenta.");
        CuentaApp anterior = cuentaAppRepository.findOne(cuentaApp.getId());
        log.debug("Anterior: " + anterior.toString());
        log.debug("Actual: " + cuentaApp.toString());
        //primero chequear que son de la misma moneda
        if (anterior.getMoneda().compareTo(cuentaApp.getMoneda()) == 0) {
            //cambio del saldo de la aplicacion
            if (anterior.getSaldo().compareTo(cuentaApp.getSaldo()) > 0) {
                BigDecimal ajuste = anterior.getSaldo().subtract(cuentaApp.getSaldo());
                log.debug("Ajuste: " + ajuste);
                actualizarSaldoApp(cuentaApp.getApp(), cuentaApp.getMoneda(), ajuste, false);
            } else if (anterior.getSaldo().compareTo(cuentaApp.getSaldo()) < 0){
                BigDecimal ajuste = cuentaApp.getSaldo().subtract(anterior.getSaldo());
                actualizarSaldoApp(cuentaApp.getApp(), cuentaApp.getMoneda(), ajuste, true);
            }
            //cambio de la cuenta
            CuentaApp result = cuentaAppRepository.save(cuentaApp);
            cuentaAppSearchRepository.save(result);
            return result;
        } else {
            //hay que realizar una conversión de monedas
            //TODO llamada al servicio de conversión de monedas y realizar un manejo adecuado de las excepciones
            log.error("No es posible cambiar de MONEDA la cuenta");
            return null;
        }
    }

    public CuentaApp update(CuentaApp cuentaApp, BigDecimal nuevoSaldo) {
        log.debug("Modificacion de Saldo de la cuenta.");
        CuentaApp anterior = cuentaAppRepository.findOne(cuentaApp.getId());
        log.debug("Anterior: " + anterior.toString());
        log.debug("Actual: " + cuentaApp.toString());
        log.debug("Nuevo saldo: " + nuevoSaldo);
        //primero chequear que son de la misma moneda
        if (anterior.getMoneda().compareTo(cuentaApp.getMoneda()) == 0) {
            //cambio del saldo de la aplicacion
            if (anterior.getSaldo().compareTo(nuevoSaldo) > 0) {
                BigDecimal ajuste = anterior.getSaldo().subtract(nuevoSaldo);
                log.debug("Ajuste: " + ajuste);
                actualizarSaldoApp(cuentaApp.getApp(), cuentaApp.getMoneda(), ajuste, false);
            } else if (anterior.getSaldo().compareTo(nuevoSaldo) < 0){
                BigDecimal ajuste = nuevoSaldo.subtract(anterior.getSaldo());
                actualizarSaldoApp(cuentaApp.getApp(), cuentaApp.getMoneda(), ajuste, true);
            }
            //cambio saldo de la cuenta
            cuentaApp.setSaldo(nuevoSaldo);
            CuentaApp result = cuentaAppRepository.save(cuentaApp);
            cuentaAppSearchRepository.save(result);
            return result;
        } else {
            //hay que realizar una conversión de monedas
            //TODO llamada al servicio de conversión de monedas y realizar un manejo adecuado de las excepciones
            log.error("No es posible cambiar de MONEDA la cuenta");
            return null;
        }
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

    @Transactional(readOnly = true)
    public CuentaApp findByAliasCbuOrCbu(String aliasCbu, String cbu){
        log.debug("Buscar por aliascbu:" +  aliasCbu + " o cbu:"+cbu);
        return cuentaAppRepository.findCuentaAppByAliasCbuOrCbu(aliasCbu, cbu);
        //return cuentaAppSearchRepository.findCuentaAppByAliasCbuOrCbu(aliasCbu, cbu);
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
