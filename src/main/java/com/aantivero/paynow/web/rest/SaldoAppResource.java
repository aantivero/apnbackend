package com.aantivero.paynow.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.SaldoApp;
import com.aantivero.paynow.service.SaldoAppService;
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
 * REST controller for managing SaldoApp.
 */
@RestController
@RequestMapping("/api")
public class SaldoAppResource {

    private final Logger log = LoggerFactory.getLogger(SaldoAppResource.class);

    private static final String ENTITY_NAME = "saldoApp";

    private final SaldoAppService saldoAppService;

    public SaldoAppResource(SaldoAppService saldoAppService) {
        this.saldoAppService = saldoAppService;
    }

    /**
     * POST  /saldo-apps : Create a new saldoApp.
     *
     * @param saldoApp the saldoApp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saldoApp, or with status 400 (Bad Request) if the saldoApp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/saldo-apps")
    @Timed
    public ResponseEntity<SaldoApp> createSaldoApp(@Valid @RequestBody SaldoApp saldoApp) throws URISyntaxException {
        log.debug("REST request to save SaldoApp : {}", saldoApp);
        if (saldoApp.getId() != null) {
            throw new BadRequestAlertException("A new saldoApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaldoApp result = saldoAppService.save(saldoApp);
        return ResponseEntity.created(new URI("/api/saldo-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /saldo-apps : Updates an existing saldoApp.
     *
     * @param saldoApp the saldoApp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saldoApp,
     * or with status 400 (Bad Request) if the saldoApp is not valid,
     * or with status 500 (Internal Server Error) if the saldoApp couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/saldo-apps")
    @Timed
    public ResponseEntity<SaldoApp> updateSaldoApp(@Valid @RequestBody SaldoApp saldoApp) throws URISyntaxException {
        log.debug("REST request to update SaldoApp : {}", saldoApp);
        if (saldoApp.getId() == null) {
            return createSaldoApp(saldoApp);
        }
        SaldoApp result = saldoAppService.save(saldoApp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saldoApp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /saldo-apps : get all the saldoApps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of saldoApps in body
     */
    @GetMapping("/saldo-apps")
    @Timed
    public ResponseEntity<List<SaldoApp>> getAllSaldoApps(Pageable pageable) {
        log.debug("REST request to get a page of SaldoApps");
        Page<SaldoApp> page = saldoAppService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/saldo-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /saldo-apps/:id : get the "id" saldoApp.
     *
     * @param id the id of the saldoApp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saldoApp, or with status 404 (Not Found)
     */
    @GetMapping("/saldo-apps/{id}")
    @Timed
    public ResponseEntity<SaldoApp> getSaldoApp(@PathVariable Long id) {
        log.debug("REST request to get SaldoApp : {}", id);
        SaldoApp saldoApp = saldoAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(saldoApp));
    }

    /**
     * DELETE  /saldo-apps/:id : delete the "id" saldoApp.
     *
     * @param id the id of the saldoApp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/saldo-apps/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaldoApp(@PathVariable Long id) {
        log.debug("REST request to delete SaldoApp : {}", id);
        saldoAppService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/saldo-apps?query=:query : search for the saldoApp corresponding
     * to the query.
     *
     * @param query the query of the saldoApp search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/saldo-apps")
    @Timed
    public ResponseEntity<List<SaldoApp>> searchSaldoApps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SaldoApps for query {}", query);
        Page<SaldoApp> page = saldoAppService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/saldo-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
