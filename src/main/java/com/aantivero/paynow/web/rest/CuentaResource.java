package com.aantivero.paynow.web.rest;

import com.aantivero.paynow.domain.User;
import com.aantivero.paynow.security.AuthoritiesConstants;
import com.aantivero.paynow.security.SecurityUtils;
import com.aantivero.paynow.service.UserService;
import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.Cuenta;
import com.aantivero.paynow.service.CuentaService;
import com.aantivero.paynow.web.rest.errors.BadRequestAlertException;
import com.aantivero.paynow.web.rest.util.HeaderUtil;
import com.aantivero.paynow.web.rest.util.PaginationUtil;
import com.aantivero.paynow.service.dto.CuentaCriteria;
import com.aantivero.paynow.service.CuentaQueryService;
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
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Cuenta.
 */
@RestController
@RequestMapping("/api")
public class CuentaResource {

    private final Logger log = LoggerFactory.getLogger(CuentaResource.class);

    private static final String ENTITY_NAME = "cuenta";

    private final CuentaService cuentaService;

    private final CuentaQueryService cuentaQueryService;

    private final UserService userService;

    public CuentaResource(CuentaService cuentaService, CuentaQueryService cuentaQueryService, UserService userService) {
        this.cuentaService = cuentaService;
        this.cuentaQueryService = cuentaQueryService;
        this.userService = userService;
    }

    /**
     * POST  /cuentas : Create a new cuenta.
     *
     * @param cuenta the cuenta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cuenta, or with status 400 (Bad Request) if the cuenta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cuentas")
    @Timed
    public ResponseEntity<Cuenta> createCuenta(@Valid @RequestBody Cuenta cuenta) throws URISyntaxException {
        log.debug("REST request to save Cuenta : {}", cuenta);
        if (cuenta.getId() != null) {
            throw new BadRequestAlertException("A new cuenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cuenta result = cuentaService.save(cuenta);
        return ResponseEntity.created(new URI("/api/cuentas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cuentas : Updates an existing cuenta.
     *
     * @param cuenta the cuenta to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cuenta,
     * or with status 400 (Bad Request) if the cuenta is not valid,
     * or with status 500 (Internal Server Error) if the cuenta couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cuentas")
    @Timed
    public ResponseEntity<Cuenta> updateCuenta(@Valid @RequestBody Cuenta cuenta) throws URISyntaxException {
        log.debug("REST request to update Cuenta : {}", cuenta);
        if (cuenta.getId() == null) {
            return createCuenta(cuenta);
        }
        Cuenta result = cuentaService.save(cuenta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cuenta.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cuentas : get all the cuentas.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of cuentas in body
     */
    @GetMapping("/cuentas")
    @Timed
    public ResponseEntity<List<Cuenta>> getAllCuentas(CuentaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cuentas by criteria: {}", criteria);
        Optional<User> userWithAuthorities = userService.getUserWithAuthorities();
        if (userWithAuthorities.isPresent()) {
            LongFilter filter = new LongFilter();
            filter.setEquals(userWithAuthorities.get().getId());
            criteria.setUsuarioId(filter);
        }
        Page<Cuenta> page = cuentaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cuentas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/cuentas-all")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<Cuenta>> getAllCuentasAdmin(CuentaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cuentas by criteria: {}", criteria);
        Page<Cuenta> page = cuentaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cuentas-all");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cuentas/:id : get the "id" cuenta.
     *
     * @param id the id of the cuenta to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cuenta, or with status 404 (Not Found)
     */
    @GetMapping("/cuentas/{id}")
    @Timed
    public ResponseEntity<Cuenta> getCuenta(@PathVariable Long id) {
        log.debug("REST request to get Cuenta : {}", id);
        Cuenta cuenta = cuentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cuenta));
    }

    /**
     * DELETE  /cuentas/:id : delete the "id" cuenta.
     *
     * @param id the id of the cuenta to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cuentas/{id}")
    @Timed
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        log.debug("REST request to delete Cuenta : {}", id);
        cuentaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cuentas?query=:query : search for the cuenta corresponding
     * to the query.
     *
     * @param query the query of the cuenta search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cuentas")
    @Timed
    public ResponseEntity<List<Cuenta>> searchCuentas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cuentas for query {}", query);
        Page<Cuenta> page = cuentaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cuentas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
