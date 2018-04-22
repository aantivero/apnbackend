package com.aantivero.paynow.service;

import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.repository.AppRepository;
import com.aantivero.paynow.repository.search.AppSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing App.
 */
@Service
@Transactional
public class AppService {

    private final Logger log = LoggerFactory.getLogger(AppService.class);

    private final AppRepository appRepository;

    private final AppSearchRepository appSearchRepository;

    public AppService(AppRepository appRepository, AppSearchRepository appSearchRepository) {
        this.appRepository = appRepository;
        this.appSearchRepository = appSearchRepository;
    }

    /**
     * Save a app.
     *
     * @param app the entity to save
     * @return the persisted entity
     */
    public App save(App app) {
        log.debug("Request to save App : {}", app);
        App result = appRepository.save(app);
        appSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the apps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<App> findAll(Pageable pageable) {
        log.debug("Request to get all Apps");
        return appRepository.findAll(pageable);
    }

    /**
     * Get one app by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public App findOne(Long id) {
        log.debug("Request to get App : {}", id);
        return appRepository.findOne(id);
    }

    /**
     * Delete the app by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete App : {}", id);
        appRepository.delete(id);
        appSearchRepository.delete(id);
    }

    /**
     * Search for the app corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<App> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Apps for query {}", query);
        Page<App> result = appSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
