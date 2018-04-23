package com.aantivero.paynow.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.CuentaApp;
import com.aantivero.paynow.service.CuentaAppService;
import com.aantivero.paynow.web.rest.errors.BadRequestAlertException;
import com.aantivero.paynow.web.rest.util.HeaderUtil;
import com.aantivero.paynow.web.rest.util.PaginationUtil;
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
 * REST controller for managing CuentaApp.
 */
@RestController
@RequestMapping("/api")
public class CuentaAppResource {

    private final Logger log = LoggerFactory.getLogger(CuentaAppResource.class);

    private static final String ENTITY_NAME = "cuentaApp";

    private final CuentaAppService cuentaAppService;

    public CuentaAppResource(CuentaAppService cuentaAppService) {
        this.cuentaAppService = cuentaAppService;
    }

    /**
     * POST  /cuenta-apps : Create a new cuentaApp.
     *
     * @param cuentaApp the cuentaApp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cuentaApp, or with status 400 (Bad Request) if the cuentaApp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cuenta-apps")
    @Timed
    public ResponseEntity<CuentaApp> createCuentaApp(@Valid @RequestBody CuentaApp cuentaApp) throws URISyntaxException {
        log.debug("REST request to save CuentaApp : {}", cuentaApp);
        if (cuentaApp.getId() != null) {
            throw new BadRequestAlertException("A new cuentaApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CuentaApp result = cuentaAppService.save(cuentaApp);
        return ResponseEntity.created(new URI("/api/cuenta-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cuenta-apps : Updates an existing cuentaApp.
     *
     * @param cuentaApp the cuentaApp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cuentaApp,
     * or with status 400 (Bad Request) if the cuentaApp is not valid,
     * or with status 500 (Internal Server Error) if the cuentaApp couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cuenta-apps")
    @Timed
    public ResponseEntity<CuentaApp> updateCuentaApp(@Valid @RequestBody CuentaApp cuentaApp) throws URISyntaxException {
        log.debug("REST request to update CuentaApp : {}", cuentaApp);
        if (cuentaApp.getId() == null) {
            return createCuentaApp(cuentaApp);
        }
        CuentaApp result = cuentaAppService.update(cuentaApp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cuentaApp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cuenta-apps : get all the cuentaApps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cuentaApps in body
     */
    @GetMapping("/cuenta-apps")
    @Timed
    public ResponseEntity<List<CuentaApp>> getAllCuentaApps(Pageable pageable) {
        log.debug("REST request to get a page of CuentaApps");
        Page<CuentaApp> page = cuentaAppService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cuenta-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cuenta-apps/:id : get the "id" cuentaApp.
     *
     * @param id the id of the cuentaApp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cuentaApp, or with status 404 (Not Found)
     */
    @GetMapping("/cuenta-apps/{id}")
    @Timed
    public ResponseEntity<CuentaApp> getCuentaApp(@PathVariable Long id) {
        log.debug("REST request to get CuentaApp : {}", id);
        CuentaApp cuentaApp = cuentaAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cuentaApp));
    }

    /**
     * DELETE  /cuenta-apps/:id : delete the "id" cuentaApp.
     *
     * @param id the id of the cuentaApp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cuenta-apps/{id}")
    @Timed
    public ResponseEntity<Void> deleteCuentaApp(@PathVariable Long id) {
        log.debug("REST request to delete CuentaApp : {}", id);
        cuentaAppService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cuenta-apps?query=:query : search for the cuentaApp corresponding
     * to the query.
     *
     * @param query the query of the cuentaApp search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cuenta-apps")
    @Timed
    public ResponseEntity<List<CuentaApp>> searchCuentaApps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CuentaApps for query {}", query);
        Page<CuentaApp> page = cuentaAppService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cuenta-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
