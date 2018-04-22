package com.aantivero.paynow.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.aantivero.paynow.domain.App;
import com.aantivero.paynow.domain.*; // for static metamodels
import com.aantivero.paynow.repository.AppRepository;
import com.aantivero.paynow.repository.search.AppSearchRepository;
import com.aantivero.paynow.service.dto.AppCriteria;


/**
 * Service for executing complex queries for App entities in the database.
 * The main input is a {@link AppCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link App} or a {@link Page} of {@link App} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppQueryService extends QueryService<App> {

    private final Logger log = LoggerFactory.getLogger(AppQueryService.class);


    private final AppRepository appRepository;

    private final AppSearchRepository appSearchRepository;

    public AppQueryService(AppRepository appRepository, AppSearchRepository appSearchRepository) {
        this.appRepository = appRepository;
        this.appSearchRepository = appSearchRepository;
    }

    /**
     * Return a {@link List} of {@link App} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<App> findByCriteria(AppCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<App> specification = createSpecification(criteria);
        return appRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link App} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<App> findByCriteria(AppCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<App> specification = createSpecification(criteria);
        return appRepository.findAll(specification, page);
    }

    /**
     * Function to convert AppCriteria to a {@link Specifications}
     */
    private Specifications<App> createSpecification(AppCriteria criteria) {
        Specifications<App> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), App_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), App_.nombre));
            }
        }
        return specification;
    }

}
