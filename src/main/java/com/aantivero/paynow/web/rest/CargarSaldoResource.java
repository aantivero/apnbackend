package com.aantivero.paynow.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.CargarSaldo;
import com.aantivero.paynow.service.CargarSaldoService;
import com.aantivero.paynow.web.rest.errors.BadRequestAlertException;
import com.aantivero.paynow.web.rest.util.HeaderUtil;
import com.aantivero.paynow.web.rest.util.PaginationUtil;
import com.aantivero.paynow.service.dto.CargarSaldoCriteria;
import com.aantivero.paynow.service.CargarSaldoQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

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

    public CargarSaldoResource(CargarSaldoService cargarSaldoService, CargarSaldoQueryService cargarSaldoQueryService) {
        this.cargarSaldoService = cargarSaldoService;
        this.cargarSaldoQueryService = cargarSaldoQueryService;
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
    public ResponseEntity<CargarSaldo> createCargarSaldo(@Valid @RequestBody CargarSaldo cargarSaldo) throws URISyntaxException {
        log.debug("REST request to save CargarSaldo : {}", cargarSaldo);
        if (cargarSaldo.getId() != null) {
            throw new BadRequestAlertException("A new cargarSaldo cannot already have an ID", ENTITY_NAME, "idexists");
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
        Page<CargarSaldo> page = cargarSaldoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cargar-saldos");
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
