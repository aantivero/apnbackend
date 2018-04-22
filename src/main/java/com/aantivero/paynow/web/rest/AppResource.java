package com.aantivero.paynow.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.service.AppService;
import com.aantivero.paynow.web.rest.errors.BadRequestAlertException;
import com.aantivero.paynow.web.rest.util.HeaderUtil;
import com.aantivero.paynow.web.rest.util.PaginationUtil;
import com.aantivero.paynow.service.dto.AppCriteria;
import com.aantivero.paynow.service.AppQueryService;
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
 * REST controller for managing App.
 */
@RestController
@RequestMapping("/api")
public class AppResource {

    private final Logger log = LoggerFactory.getLogger(AppResource.class);

    private static final String ENTITY_NAME = "app";

    private final AppService appService;

    private final AppQueryService appQueryService;

    public AppResource(AppService appService, AppQueryService appQueryService) {
        this.appService = appService;
        this.appQueryService = appQueryService;
    }

    /**
     * POST  /apps : Create a new app.
     *
     * @param app the app to create
     * @return the ResponseEntity with status 201 (Created) and with body the new app, or with status 400 (Bad Request) if the app has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apps")
    @Timed
    public ResponseEntity<App> createApp(@Valid @RequestBody App app) throws URISyntaxException {
        log.debug("REST request to save App : {}", app);
        if (app.getId() != null) {
            throw new BadRequestAlertException("A new app cannot already have an ID", ENTITY_NAME, "idexists");
        }
        App result = appService.save(app);
        return ResponseEntity.created(new URI("/api/apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apps : Updates an existing app.
     *
     * @param app the app to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated app,
     * or with status 400 (Bad Request) if the app is not valid,
     * or with status 500 (Internal Server Error) if the app couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apps")
    @Timed
    public ResponseEntity<App> updateApp(@Valid @RequestBody App app) throws URISyntaxException {
        log.debug("REST request to update App : {}", app);
        if (app.getId() == null) {
            return createApp(app);
        }
        App result = appService.save(app);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, app.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apps : get all the apps.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of apps in body
     */
    @GetMapping("/apps")
    @Timed
    public ResponseEntity<List<App>> getAllApps(AppCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Apps by criteria: {}", criteria);
        Page<App> page = appQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /apps/:id : get the "id" app.
     *
     * @param id the id of the app to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the app, or with status 404 (Not Found)
     */
    @GetMapping("/apps/{id}")
    @Timed
    public ResponseEntity<App> getApp(@PathVariable Long id) {
        log.debug("REST request to get App : {}", id);
        App app = appService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(app));
    }

    /**
     * DELETE  /apps/:id : delete the "id" app.
     *
     * @param id the id of the app to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apps/{id}")
    @Timed
    public ResponseEntity<Void> deleteApp(@PathVariable Long id) {
        log.debug("REST request to delete App : {}", id);
        appService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/apps?query=:query : search for the app corresponding
     * to the query.
     *
     * @param query the query of the app search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/apps")
    @Timed
    public ResponseEntity<List<App>> searchApps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Apps for query {}", query);
        Page<App> page = appService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
