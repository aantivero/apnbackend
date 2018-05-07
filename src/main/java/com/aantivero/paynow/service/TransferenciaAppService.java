package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.CuentaApp;
import com.aantivero.paynow.domain.MovimientoApp;
import com.aantivero.paynow.domain.TransferenciaApp;
import com.aantivero.paynow.domain.enumeration.EstadoTransferencia;
import com.aantivero.paynow.domain.enumeration.TipoTransferencia;
import com.aantivero.paynow.repository.TransferenciaAppRepository;
import com.aantivero.paynow.repository.search.TransferenciaAppSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;

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

    private final CuentaAppService cuentaAppService;

    private final MovimientoAppService movimientoAppService;

    public TransferenciaAppService(TransferenciaAppRepository transferenciaAppRepository,
                                   TransferenciaAppSearchRepository transferenciaAppSearchRepository,
                                   CuentaAppService cuentaAppService,
                                   MovimientoAppService movimientoAppService) {
        this.transferenciaAppRepository = transferenciaAppRepository;
        this.transferenciaAppSearchRepository = transferenciaAppSearchRepository;
        this.cuentaAppService = cuentaAppService;
        this.movimientoAppService = movimientoAppService;
    }

    /**
     * Save a transferenciaApp.
     *
     * @param transferenciaApp the entity to save
     * @return the persisted entity
     */
    public TransferenciaApp save(TransferenciaApp transferenciaApp) {
        log.debug("Request to save TransferenciaApp : {}", transferenciaApp);
        //verficar que la cuenta de origen tiene saldo
        CuentaApp cuentaApp = cuentaAppService.findOne(transferenciaApp.getOrigen().getId());
        if(cuentaApp.getSaldo().compareTo(transferenciaApp.getMonto()) < 0 && transferenciaApp.getTipoTransferencia().equals(TipoTransferencia.TRANSFERENCIA)) {
            //TODO falta manejo de excepciones del tipo de negocio
            log.error("La cuenta " + cuentaApp + " no tiene saldo suficiente: " + transferenciaApp.getMonto());
            return null;
        }
        //verificar que la moneda es la misma que en la cuenta
        if(!cuentaApp.getMoneda().equals(transferenciaApp.getMoneda())){
            log.error("La moneda de la cuenta no es la misma que la transferencia");
            return null;
        }
        //verificar que existe cbu o alias destino
        if(transferenciaApp.getDestinoAlias() == null && transferenciaApp.getDestinoCbu() == null){
            //TODO crear una forma mas adecuada de comprobar que tiene alias o cbu por el momento solo no nulo
            log.error("No tiene definido ni alias ni cbu destino");
            return null;
        }
        /*if(transferenciaApp.getDestinoAlias().isEmpty() && transferenciaApp.getDestinoCbu().isEmpty()){
            log.error("No tiene definido ni alias ni cbu destino");
            return null;
        }*/
        //si la transferencia es 'aceptada'se genera un movimiento app
        if (transferenciaApp.getEstadoTransferencia().equals(EstadoTransferencia.ACEPTADA)){
            log.debug("Crear el movimiento dependiendo del tipo de transferencia");
            MovimientoApp movimientoApp = null;
            if (transferenciaApp.getTipoTransferencia().equals(TipoTransferencia.DEBIN)) {
                movimientoApp = crearMovimientoDebin(transferenciaApp);
            } else if(transferenciaApp.getTipoTransferencia().equals(TipoTransferencia.TRANSFERENCIA)) {
                movimientoApp = crearMovimientoTransferencia(transferenciaApp);
            }
            if(movimientoApp!=null){
                MovimientoApp result = movimientoAppService.save(movimientoApp);
                log.debug("Movimiento creado:"+result);
            }
        }
        TransferenciaApp result = transferenciaAppRepository.save(transferenciaApp);
        transferenciaAppSearchRepository.save(result);
        return result;
    }

    private MovimientoApp crearMovimientoDebin(TransferenciaApp transferenciaApp) {
        MovimientoApp movimientoApp = new MovimientoApp();
        //credito
        movimientoApp.setCuentaCreditoAlias(transferenciaApp.getOrigen().getAliasCbu());
        movimientoApp.setCuentaCreditoCbu(transferenciaApp.getOrigen().getCbu());
        movimientoApp.setCuentaCreditoPropia(true);
        movimientoApp.setBancoCredito(transferenciaApp.getOrigen().getBanco());
        //debito
        movimientoApp.setCuentaDebitoAlias(transferenciaApp.getDestinoAlias());
        movimientoApp.setCuentaDebitoCbu(transferenciaApp.getDestinoCbu());
        movimientoApp.setCuentaDebitoPropia(false);
        movimientoApp.setBancoDebito(transferenciaApp.getDestinoBanco());
        //
        movimientoApp.setMoneda(transferenciaApp.getMoneda());
        movimientoApp.setMonto(transferenciaApp.getMonto());
        movimientoApp.setDescripcion(transferenciaApp.getDescripcion());
        movimientoApp.setConsolidado(false);
        movimientoApp.setTimestamp(Instant.now());
        movimientoApp.setIdentificacion(transferenciaApp.getIdentificacion());
        return movimientoApp;
    }

    private MovimientoApp crearMovimientoTransferencia(TransferenciaApp transferenciaApp) {
        MovimientoApp movimientoApp = new MovimientoApp();
        //debito
        movimientoApp.setCuentaDebitoAlias(transferenciaApp.getOrigen().getAliasCbu());
        movimientoApp.setCuentaDebitoCbu(transferenciaApp.getOrigen().getCbu());
        movimientoApp.setCuentaDebitoPropia(true);
        movimientoApp.setBancoDebito(transferenciaApp.getOrigen().getBanco());
        //credito
        movimientoApp.setCuentaCreditoAlias(transferenciaApp.getDestinoAlias());
        movimientoApp.setCuentaCreditoCbu(transferenciaApp.getDestinoCbu());
        movimientoApp.setCuentaCreditoPropia(false);
        movimientoApp.setBancoCredito(transferenciaApp.getDestinoBanco());
        //
        movimientoApp.setMoneda(transferenciaApp.getMoneda());
        movimientoApp.setMonto(transferenciaApp.getMonto());
        movimientoApp.setDescripcion(transferenciaApp.getDescripcion());
        movimientoApp.setConsolidado(false);
        movimientoApp.setTimestamp(Instant.now());
        movimientoApp.setIdentificacion(transferenciaApp.getIdentificacion());
        return movimientoApp;
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
