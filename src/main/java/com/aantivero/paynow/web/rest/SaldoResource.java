package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.domain.User;
import com.aantivero.paynow.service.UserService;
import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.Saldo;
import com.aantivero.paynow.service.SaldoService;
import com.aantivero.paynow.web.rest.errors.BadRequestAlertException;
import com.aantivero.paynow.web.rest.util.HeaderUtil;
import com.aantivero.paynow.web.rest.util.PaginationUtil;
import com.aantivero.paynow.service.dto.SaldoCriteria;
import com.aantivero.paynow.service.SaldoQueryService;
import io.github.jhipster.service.filter.LongFilter;
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
 * REST controller for managing Saldo.
 */
@RestController
@RequestMapping("/api")
public class SaldoResource {

    private final Logger log = LoggerFactory.getLogger(SaldoResource.class);

    private static final String ENTITY_NAME = "saldo";

    private final SaldoService saldoService;

    private final SaldoQueryService saldoQueryService;

    private final UserService userService;

    public SaldoResource(SaldoService saldoService, SaldoQueryService saldoQueryService, UserService userService) {
        this.saldoService = saldoService;
        this.saldoQueryService = saldoQueryService;
        this.userService = userService;
    }

    /**
     * POST  /saldos : Create a new saldo.
     *
     * @param saldo the saldo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saldo, or with status 400 (Bad Request) if the saldo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/saldos")
    @Timed
    public ResponseEntity<Saldo> createSaldo(@Valid @RequestBody Saldo saldo) throws URISyntaxException {
        log.debug("REST request to save Saldo : {}", saldo);
        if (saldo.getId() != null) {
            throw new BadRequestAlertException("A new saldo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Saldo result = saldoService.save(saldo);
        return ResponseEntity.created(new URI("/api/saldos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /saldos : Updates an existing saldo.
     *
     * @param saldo the saldo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saldo,
     * or with status 400 (Bad Request) if the saldo is not valid,
     * or with status 500 (Internal Server Error) if the saldo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/saldos")
    @Timed
    public ResponseEntity<Saldo> updateSaldo(@Valid @RequestBody Saldo saldo) throws URISyntaxException {
        log.debug("REST request to update Saldo : {}", saldo);
        if (saldo.getId() == null) {
            return createSaldo(saldo);
        }
        Saldo result = saldoService.save(saldo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saldo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /saldos : get all the saldos.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of saldos in body
     */
    @GetMapping("/saldos")
    @Timed
    public ResponseEntity<List<Saldo>> getAllSaldos(SaldoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Saldos by criteria: {}", criteria);
        Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
        if (userWithAuthorities.isPresent()) {
            LongFilter filter = new LongFilter();
            filter.setEquals(userWithAuthorities.get().getId());
            criteria.setUsuarioId(filter);
        }
        Page<Saldo> page = saldoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/saldos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /saldos/:id : get the "id" saldo.
     *
     * @param id the id of the saldo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saldo, or with status 404 (Not Found)
     */
    @GetMapping("/saldos/{id}")
    @Timed
    public ResponseEntity<Saldo> getSaldo(@PathVariable Long id) {
        log.debug("REST request to get Saldo : {}", id);
        Saldo saldo = saldoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(saldo));
    }

    /**
     * DELETE  /saldos/:id : delete the "id" saldo.
     *
     * @param id the id of the saldo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/saldos/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaldo(@PathVariable Long id) {
        log.debug("REST request to delete Saldo : {}", id);
        saldoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/saldos?query=:query : search for the saldo corresponding
     * to the query.
     *
     * @param query the query of the saldo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/saldos")
    @Timed
    public ResponseEntity<List<Saldo>> searchSaldos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Saldos for query {}", query);
        Page<Saldo> page = saldoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/saldos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
