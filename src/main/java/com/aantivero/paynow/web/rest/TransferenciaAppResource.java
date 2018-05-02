package com.aantivero.paynow.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.TransferenciaApp;
import com.aantivero.paynow.service.TransferenciaAppService;
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
 * REST controller for managing TransferenciaApp.
 */
@RestController
@RequestMapping("/api")
public class TransferenciaAppResource {

    private final Logger log = LoggerFactory.getLogger(TransferenciaAppResource.class);

    private static final String ENTITY_NAME = "transferenciaApp";

    private final TransferenciaAppService transferenciaAppService;

    public TransferenciaAppResource(TransferenciaAppService transferenciaAppService) {
        this.transferenciaAppService = transferenciaAppService;
    }

    /**
     * POST  /transferencia-apps : Create a new transferenciaApp.
     *
     * @param transferenciaApp the transferenciaApp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transferenciaApp, or with status 400 (Bad Request) if the transferenciaApp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transferencia-apps")
    @Timed
    public ResponseEntity<TransferenciaApp> createTransferenciaApp(@Valid @RequestBody TransferenciaApp transferenciaApp) throws URISyntaxException {
        log.debug("REST request to save TransferenciaApp : {}", transferenciaApp);
        if (transferenciaApp.getId() != null) {
            throw new BadRequestAlertException("A new transferenciaApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransferenciaApp result = transferenciaAppService.save(transferenciaApp);
        if (result == null){
            throw new BadRequestAlertException("No se pudo crear la transferencia", ENTITY_NAME, "crearTransferencia");
        }
        return ResponseEntity.created(new URI("/api/transferencia-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transferencia-apps : Updates an existing transferenciaApp.
     *
     * @param transferenciaApp the transferenciaApp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transferenciaApp,
     * or with status 400 (Bad Request) if the transferenciaApp is not valid,
     * or with status 500 (Internal Server Error) if the transferenciaApp couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transferencia-apps")
    @Timed
    public ResponseEntity<TransferenciaApp> updateTransferenciaApp(@Valid @RequestBody TransferenciaApp transferenciaApp) throws URISyntaxException {
        log.debug("REST request to update TransferenciaApp : {}", transferenciaApp);
        if (transferenciaApp.getId() == null) {
            return createTransferenciaApp(transferenciaApp);
        }
        TransferenciaApp result = transferenciaAppService.save(transferenciaApp);
        if (result == null){
            throw new BadRequestAlertException("No se pudo modificar la transferencia", ENTITY_NAME, "modificarTransferencia");
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transferenciaApp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transferencia-apps : get all the transferenciaApps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transferenciaApps in body
     */
    @GetMapping("/transferencia-apps")
    @Timed
    public ResponseEntity<List<TransferenciaApp>> getAllTransferenciaApps(Pageable pageable) {
        log.debug("REST request to get a page of TransferenciaApps");
        Page<TransferenciaApp> page = transferenciaAppService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transferencia-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transferencia-apps/:id : get the "id" transferenciaApp.
     *
     * @param id the id of the transferenciaApp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transferenciaApp, or with status 404 (Not Found)
     */
    @GetMapping("/transferencia-apps/{id}")
    @Timed
    public ResponseEntity<TransferenciaApp> getTransferenciaApp(@PathVariable Long id) {
        log.debug("REST request to get TransferenciaApp : {}", id);
        TransferenciaApp transferenciaApp = transferenciaAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transferenciaApp));
    }

    /**
     * DELETE  /transferencia-apps/:id : delete the "id" transferenciaApp.
     *
     * @param id the id of the transferenciaApp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transferencia-apps/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransferenciaApp(@PathVariable Long id) {
        log.debug("REST request to delete TransferenciaApp : {}", id);
        transferenciaAppService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transferencia-apps?query=:query : search for the transferenciaApp corresponding
     * to the query.
     *
     * @param query the query of the transferenciaApp search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transferencia-apps")
    @Timed
    public ResponseEntity<List<TransferenciaApp>> searchTransferenciaApps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TransferenciaApps for query {}", query);
        Page<TransferenciaApp> page = transferenciaAppService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/transferencia-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
