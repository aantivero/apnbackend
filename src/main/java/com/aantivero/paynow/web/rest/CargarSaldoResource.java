package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.domain.*;
import com.aantivero.paynow.domain.enumeration.EstadoTransferencia;
import com.aantivero.paynow.domain.enumeration.TipoCuenta;
import com.aantivero.paynow.domain.enumeration.TipoTransferencia;
import com.aantivero.paynow.security.AuthoritiesConstants;
import com.aantivero.paynow.service.*;
import com.aantivero.paynow.service.dto.CuentaCriteria;
import com.aantivero.paynow.service.dto.SaldoCriteria;
import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.web.rest.errors.BadRequestAlertException;
import com.aantivero.paynow.web.rest.util.HeaderUtil;
import com.aantivero.paynow.web.rest.util.PaginationUtil;
import com.aantivero.paynow.service.dto.CargarSaldoCriteria;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CargarSaldo.
 */
@RestController
@RequestMapping("/api")
public class CargarSaldoResource {

    private final Logger log = LoggerFactory.getLogger(CargarSaldoResource.class);

    private static final String ENTITY_NAME = "cargarSaldo";

    private final CargarSaldoService cargarSaldoService;

    private final CargarSaldoQueryService cargarSaldoQueryService;

    private final UserService userService;

    private final TransferenciaAppService transferenciaAppService;

    private final CuentaAppService cuentaAppService;

    private final SaldoService saldoService;

    private final SaldoQueryService saldoQueryService;

    private final CuentaService cuentaService;

    private final CuentaQueryService cuentaQueryService;

    private final AppService appService;

    public CargarSaldoResource(CargarSaldoService cargarSaldoService, CargarSaldoQueryService cargarSaldoQueryService,
                               UserService userService, TransferenciaAppService transferenciaAppService,
                               CuentaAppService cuentaAppService, SaldoService saldoService, SaldoQueryService saldoQueryService,
                               CuentaService cuentaService, CuentaQueryService cuentaQueryService, AppService appService) {
        this.cargarSaldoService = cargarSaldoService;
        this.cargarSaldoQueryService = cargarSaldoQueryService;
        this.userService = userService;
        this.transferenciaAppService = transferenciaAppService;
        this.cuentaAppService = cuentaAppService;
        this.saldoService = saldoService;
        this.saldoQueryService = saldoQueryService;
        this.cuentaService = cuentaService;
        this.cuentaQueryService = cuentaQueryService;
        this.appService = appService;
    }

    /**
     * POST  /cargar-saldos : Create a new cargarSaldo.
     *
     * @param cargarSaldo the cargarSaldo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cargarSaldo, or with status 400 (Bad Request) if the cargarSaldo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cargar-saldos")
    @Timed
    public ResponseEntity<CargarSaldo> createCargarSaldo(@RequestBody CargarSaldo cargarSaldo) throws URISyntaxException {
        log.debug("REST request to save CargarSaldo : {}", cargarSaldo);
        if (cargarSaldo.getId() != null) {
            throw new BadRequestAlertException("A new cargarSaldo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (cargarSaldo.getMonto() != null && cargarSaldo.getCuenta() != null) {
            Optional<User> user = userService.getUserWithAuthorities();
            if (user.isPresent()) {
                //setear el usuario actual
                cargarSaldo.setUsuario(user.get());
                //crear una transferencia App DEBIN
                CuentaApp cuentaApp = cuentaAppService.findAll(null).getContent().get(0);
                TransferenciaApp transferenciaApp = new TransferenciaApp();
                transferenciaApp.setOrigen(cuentaApp);
                transferenciaApp.setDestinoCbu(cargarSaldo.getCuenta().getCbu());
                transferenciaApp.setDestinoAlias(cargarSaldo.getCuenta().getAliasCbu());
                transferenciaApp.setMoneda(cargarSaldo.getCuenta().getMoneda());
                transferenciaApp.setMonto(cargarSaldo.getMonto());
                transferenciaApp.setDescripcion("CARGA DE SALDO");
                transferenciaApp.setEstadoTransferencia(EstadoTransferencia.ACEPTADA);
                transferenciaApp.setTipoTransferencia(TipoTransferencia.DEBIN);
                transferenciaApp.setDestinoBanco(cargarSaldo.getCuenta().getBanco());
                transferenciaApp.setTimestamp(Instant.now());
                transferenciaApp = transferenciaAppService.save(transferenciaApp);
                cargarSaldo.setTransferenciaApp(transferenciaApp);

                //cargar el saldo virtual de la cuenta del usuario
                SaldoCriteria saldoCriteria = new SaldoCriteria();
                LongFilter usuarioIdFilter = new LongFilter();
                usuarioIdFilter.setEquals(user.get().getId());
                saldoCriteria.setUsuarioId(usuarioIdFilter);
                SaldoCriteria.MonedaFilter monedaFilter = new SaldoCriteria.MonedaFilter();
                monedaFilter.setEquals(cargarSaldo.getCuenta().getMoneda());
                saldoCriteria.setMoneda(monedaFilter);
                SaldoCriteria.TipoCuentaFilter tipoCuentaFilter = new SaldoCriteria.TipoCuentaFilter();
                tipoCuentaFilter.setEquals(TipoCuenta.VIRTUAL);
                saldoCriteria.setTipo(tipoCuentaFilter);
                List<Saldo> saldos = saldoQueryService.findByCriteria(saldoCriteria);
                if (!saldos.isEmpty()) {
                    Saldo saldo = saldos.get(0);
                    saldo.setMonto(saldo.getMonto().add(cargarSaldo.getMonto()));
                    saldo.setFechaModificacion(Instant.now());
                    saldoService.save(saldo);
                } else {
                    Saldo saldo = new Saldo();
                    saldo.setMonto(cargarSaldo.getMonto());
                    saldo.setMoneda(cargarSaldo.getCuenta().getMoneda());
                    saldo.setTipo(TipoCuenta.VIRTUAL);
                    saldo.setFechaCreacion(Instant.now());
                    saldo.setFechaModificacion(Instant.now());
                    saldo.setUsuario(user.get());
                    saldo.setAplicacion(appService.findAll(null).getContent().get(0));
                    saldoService.save(saldo);
                }

                //aumentar el saldo de la cuenta virtual
                CuentaCriteria cuentaCriteria = new CuentaCriteria();
                cuentaCriteria.setUsuarioId(usuarioIdFilter);
                CuentaCriteria.TipoCuentaFilter tipoCuentaFilter1 = new CuentaCriteria.TipoCuentaFilter();
                tipoCuentaFilter1.setEquals(TipoCuenta.VIRTUAL);
                cuentaCriteria.setTipo(tipoCuentaFilter1);
                CuentaCriteria.MonedaFilter monedaFilter1 = new CuentaCriteria.MonedaFilter();
                monedaFilter1.setEquals(cargarSaldo.getCuenta().getMoneda());
                cuentaCriteria.setMoneda(monedaFilter1);
                List<Cuenta> cuentaList = cuentaQueryService.findByCriteria(cuentaCriteria);
                if (!cuentaList.isEmpty()) {
                    Cuenta cuenta = cuentaList.get(0);
                    cuenta.setSaldo(cuenta.getSaldo().add(cargarSaldo.getMonto()));
                    cuentaService.save(cuenta);
                } else {
                    Cuenta cuenta = new Cuenta();
                    cuenta.setUsuario(user.get());
                    cuenta.setNombre(user.get().getLogin()+"CuentaVirtual" + cargarSaldo.getCuenta().getMoneda());
                    cuenta.setMoneda(cargarSaldo.getCuenta().getMoneda());
                    cuenta.setSaldo(cargarSaldo.getMonto());
                    cuenta.setTipo(TipoCuenta.VIRTUAL);
                    cuenta.setFechaCreacion(Instant.now());
                    cuenta.setFechaModificacion(Instant.now());
                    cuenta.setHabilitada(true);
                    cuenta.setParaCredito(true);
                    cuenta.setParaDebito(true);
                    cuentaService.save(cuenta);

                }
            }
        }
        CargarSaldo result = cargarSaldoService.save(cargarSaldo);
        return ResponseEntity.created(new URI("/api/cargar-saldos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cargar-saldos : Updates an existing cargarSaldo.
     *
     * @param cargarSaldo the cargarSaldo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cargarSaldo,
     * or with status 400 (Bad Request) if the cargarSaldo is not valid,
     * or with status 500 (Internal Server Error) if the cargarSaldo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cargar-saldos")
    @Timed
    public ResponseEntity<CargarSaldo> updateCargarSaldo(@Valid @RequestBody CargarSaldo cargarSaldo) throws URISyntaxException {
        log.debug("REST request to update CargarSaldo : {}", cargarSaldo);
        if (cargarSaldo.getId() == null) {
            return createCargarSaldo(cargarSaldo);
        }
        CargarSaldo result = cargarSaldoService.save(cargarSaldo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cargarSaldo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cargar-saldos : get all the cargarSaldos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of cargarSaldos in body
     */
    @GetMapping("/cargar-saldos")
    @Timed
    public ResponseEntity<List<CargarSaldo>> getAllCargarSaldos(CargarSaldoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CargarSaldos by criteria: {}", criteria);
        Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
        if (userWithAuthorities.isPresent()) {
            LongFilter filter = new LongFilter();
            filter.setEquals(userWithAuthorities.get().getId());
            criteria.setUsuarioId(filter);
        }
        Page<CargarSaldo> page = cargarSaldoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cargar-saldos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/cargar-saldos-all")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<CargarSaldo>> getAllCargarSaldosAdmin(CargarSaldoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CargarSaldos by criteria: {}", criteria);
        Page<CargarSaldo> page = cargarSaldoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cargar-saldos-all");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cargar-saldos/:id : get the "id" cargarSaldo.
     *
     * @param id the id of the cargarSaldo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cargarSaldo, or with status 404 (Not Found)
     */
    @GetMapping("/cargar-saldos/{id}")
    @Timed
    public ResponseEntity<CargarSaldo> getCargarSaldo(@PathVariable Long id) {
        log.debug("REST request to get CargarSaldo : {}", id);
        CargarSaldo cargarSaldo = cargarSaldoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cargarSaldo));
    }

    /**
     * DELETE  /cargar-saldos/:id : delete the "id" cargarSaldo.
     *
     * @param id the id of the cargarSaldo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cargar-saldos/{id}")
    @Timed
    public ResponseEntity<Void> deleteCargarSaldo(@PathVariable Long id) {
        log.debug("REST request to delete CargarSaldo : {}", id);
        cargarSaldoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cargar-saldos?query=:query : search for the cargarSaldo corresponding
     * to the query.
     *
     * @param query the query of the cargarSaldo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cargar-saldos")
    @Timed
    public ResponseEntity<List<CargarSaldo>> searchCargarSaldos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CargarSaldos for query {}", query);
        Page<CargarSaldo> page = cargarSaldoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cargar-saldos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
