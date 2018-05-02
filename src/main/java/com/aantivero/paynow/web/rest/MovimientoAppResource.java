package com.aantivero.paynow.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.MovimientoApp;
import com.aantivero.paynow.service.MovimientoAppService;
import com.aantivero.paynow.web.rest.errors.BadRequestAlertException;
import com.aantivero.paynow.web.rest.util.HeaderUtil;
import com.aantivero.paynow.web.rest.util.PaginationUtil;
import com.aantivero.paynow.service.dto.MovimientoAppCriteria;
import com.aantivero.paynow.service.MovimientoAppQueryService;
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
 * REST controller for managing MovimientoApp.
 */
@RestController
@RequestMapping("/api")
public class MovimientoAppResource {

    private final Logger log = LoggerFactory.getLogger(MovimientoAppResource.class);

    private static final String ENTITY_NAME = "movimientoApp";

    private final MovimientoAppService movimientoAppService;

    private final MovimientoAppQueryService movimientoAppQueryService;

    public MovimientoAppResource(MovimientoAppService movimientoAppService, MovimientoAppQueryService movimientoAppQueryService) {
        this.movimientoAppService = movimientoAppService;
        this.movimientoAppQueryService = movimientoAppQueryService;
    }

    /**
     * POST  /movimiento-apps : Create a new movimientoApp.
     *
     * @param movimientoApp the movimientoApp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movimientoApp, or with status 400 (Bad Request) if the movimientoApp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/movimiento-apps")
    @Timed
    public ResponseEntity<MovimientoApp> createMovimientoApp(@Valid @RequestBody MovimientoApp movimientoApp) throws URISyntaxException {
        log.debug("REST request to save MovimientoApp : {}", movimientoApp);
        if (movimientoApp.getId() != null) {
            throw new BadRequestAlertException("A new movimientoApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovimientoApp result = movimientoAppService.save(movimientoApp);
        return ResponseEntity.created(new URI("/api/movimiento-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movimiento-apps : Updates an existing movimientoApp.
     *
     * @param movimientoApp the movimientoApp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movimientoApp,
     * or with status 400 (Bad Request) if the movimientoApp is not valid,
     * or with status 500 (Internal Server Error) if the movimientoApp couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/movimiento-apps")
    @Timed
    public ResponseEntity<MovimientoApp> updateMovimientoApp(@Valid @RequestBody MovimientoApp movimientoApp) throws URISyntaxException {
        log.debug("REST request to update MovimientoApp : {}", movimientoApp);
        if (movimientoApp.getId() == null) {
            return createMovimientoApp(movimientoApp);
        }
        MovimientoApp result = movimientoAppService.save(movimientoApp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, movimientoApp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movimiento-apps : get all the movimientoApps.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of movimientoApps in body
     */
    @GetMapping("/movimiento-apps")
    @Timed
    public ResponseEntity<List<MovimientoApp>> getAllMovimientoApps(MovimientoAppCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MovimientoApps by criteria: {}", criteria);
        Page<MovimientoApp> page = movimientoAppQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movimiento-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /movimiento-apps/:id : get the "id" movimientoApp.
     *
     * @param id the id of the movimientoApp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movimientoApp, or with status 404 (Not Found)
     */
    @GetMapping("/movimiento-apps/{id}")
    @Timed
    public ResponseEntity<MovimientoApp> getMovimientoApp(@PathVariable Long id) {
        log.debug("REST request to get MovimientoApp : {}", id);
        MovimientoApp movimientoApp = movimientoAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(movimientoApp));
    }

    /**
     * DELETE  /movimiento-apps/:id : delete the "id" movimientoApp.
     *
     * @param id the id of the movimientoApp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/movimiento-apps/{id}")
    @Timed
    public ResponseEntity<Void> deleteMovimientoApp(@PathVariable Long id) {
        log.debug("REST request to delete MovimientoApp : {}", id);
        movimientoAppService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/movimiento-apps?query=:query : search for the movimientoApp corresponding
     * to the query.
     *
     * @param query the query of the movimientoApp search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/movimiento-apps")
    @Timed
    public ResponseEntity<List<MovimientoApp>> searchMovimientoApps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MovimientoApps for query {}", query);
        Page<MovimientoApp> page = movimientoAppService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/movimiento-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
