package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.Cuenta;
import com.aantivero.paynow.domain.Saldo;
import com.aantivero.paynow.domain.User;
import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.domain.enumeration.TipoCuenta;
import com.aantivero.paynow.repository.CuentaRepository;
import com.aantivero.paynow.repository.search.CuentaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Cuenta.
 */
@Service
@Transactional
public class CuentaService {

    private final Logger log = LoggerFactory.getLogger(CuentaService.class);

    private final CuentaRepository cuentaRepository;

    private final CuentaSearchRepository cuentaSearchRepository;

    private final SaldoService saldoService;

    private final AppService appService;

    public CuentaService(CuentaRepository cuentaRepository, CuentaSearchRepository cuentaSearchRepository,
                         SaldoService saldoService, AppService appService) {
        this.cuentaRepository = cuentaRepository;
        this.cuentaSearchRepository = cuentaSearchRepository;
        this.saldoService = saldoService;
        this.appService = appService;
    }

    /**
     * Save a cuenta.
     *
     * @param cuenta the entity to save
     * @return the persisted entity
     */
    public Cuenta save(Cuenta cuenta) {
        log.debug("Request to save Cuenta : {}", cuenta);
        Cuenta result = cuentaRepository.save(cuenta);
        cuentaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cuentas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cuenta> findAll(Pageable pageable) {
        log.debug("Request to get all Cuentas");
        return cuentaRepository.findAll(pageable);
    }

    /**
     * Get one cuenta by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Cuenta findOne(Long id) {
        log.debug("Request to get Cuenta : {}", id);
        return cuentaRepository.findOne(id);
    }

    /**
     * Delete the cuenta by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cuenta : {}", id);
        cuentaRepository.delete(id);
        cuentaSearchRepository.delete(id);
    }

    /**
     * Search for the cuenta corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cuenta> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cuentas for query {}", query);
        Page<Cuenta> result = cuentaSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     * Crear cuenta virtual en pesos y dolares ambas se encuentran deshabilitdas
     * @param user
     */
    public void crearCuentaVirtualDeshabilitada(User user) {
        crearCuentaVirtualDeshabilitada(user, Moneda.PESOS);
        crearCuentaVirtualDeshabilitada(user, Moneda.DOLAR);

    }

    /**
     * Habilitar cuenta virtuales en pesos y dolar,
     * Crear un saldo
     * @param user
     */
    public void habilitarCuentaVirtual(User user) {
        habilitarCuentaVirtual(user, Moneda.PESOS);
        habilitarCuentaVirtual(user, Moneda.DOLAR);
    }

    public void deshabilitarCuentaVirtual(User user) {
        deshabilitarCuentaVirtual(user, Moneda.PESOS);
        deshabilitarCuentaVirtual(user, Moneda.DOLAR);
    }

    private void deshabilitarCuentaVirtual(User user, Moneda moneda) {
        cuentaRepository.
            findOneCuentaByUsuarioAndMonedaAndTipoAndHabilitada(user, moneda, TipoCuenta.VIRTUAL, true)
            .map(cuenta -> {
                cuenta.setHabilitada(false);
                cuenta.setParaDebito(false);
                cuenta.setParaCredito(false);
                cuenta.setFechaModificacion(Instant.now());
                cuentaSearchRepository.save(cuenta);
                log.debug("Deshabilitada la cuenta virtual: {}", cuenta);
                return cuenta;
            });
    }

    private void habilitarCuentaVirtual(User user, Moneda moneda) {
        Optional<Cuenta> result = cuentaRepository.
            findOneCuentaByUsuarioAndMonedaAndTipoAndHabilitada(user, moneda, TipoCuenta.VIRTUAL, false)
            .map(cuenta -> {
                cuenta.setHabilitada(true);
                cuenta.setParaDebito(true);
                cuenta.setParaCredito(true);
                cuenta.setFechaModificacion(Instant.now());
                cuentaSearchRepository.save(cuenta);
                log.debug("Habilitada la cuenta virtual: {}", cuenta);
                return cuenta;
            });
        if (result.isPresent()){
            Saldo saldo = new Saldo();
            saldo.setAplicacion(appService.findAll(null).getContent().get(0));
            saldo.setUsuario(user);
            saldo.setMonto(BigDecimal.ZERO);
            saldo.setMoneda(moneda);
            saldo.setTipo(TipoCuenta.VIRTUAL);
            saldo.setFechaCreacion(Instant.now());
            saldo.setFechaModificacion(Instant.now());
            saldo = saldoService.save(saldo);
            log.debug("Creado el saldo virtual: {}", saldo);
        }
    }

    private void crearCuentaVirtualDeshabilitada(User user, Moneda moneda) {
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(user);
        cuenta.setNombre(user.getLogin()+"CuentaVirtual" + moneda);
        cuenta.setMoneda(moneda);
        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setTipo(TipoCuenta.VIRTUAL);
        cuenta.setFechaCreacion(Instant.now());
        cuenta.setFechaModificacion(Instant.now());
        cuenta.setHabilitada(false);
        cuenta.setParaCredito(false);
        cuenta.setParaDebito(false);
        cuenta = save(cuenta);
        log.debug("Cuenta Virtual Dolar: {}", cuenta);
    }
}
