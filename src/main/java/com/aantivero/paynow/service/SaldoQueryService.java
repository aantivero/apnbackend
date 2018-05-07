package com.aantivero.paynow.service;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.aantivero.paynow.domain.Saldo;
import com.aantivero.paynow.domain.*; // for static metamodels
import com.aantivero.paynow.repository.SaldoRepository;
import com.aantivero.paynow.repository.search.SaldoSearchRepository;
import com.aantivero.paynow.service.dto.SaldoCriteria;

import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.domain.enumeration.TipoCuenta;

/**
 * Service for executing complex queries for Saldo entities in the database.
 * The main input is a {@link SaldoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Saldo} or a {@link Page} of {@link Saldo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaldoQueryService extends QueryService<Saldo> {

    private final Logger log = LoggerFactory.getLogger(SaldoQueryService.class);


    private final SaldoRepository saldoRepository;

    private final SaldoSearchRepository saldoSearchRepository;

    public SaldoQueryService(SaldoRepository saldoRepository, SaldoSearchRepository saldoSearchRepository) {
        this.saldoRepository = saldoRepository;
        this.saldoSearchRepository = saldoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Saldo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Saldo> findByCriteria(SaldoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Saldo> specification = createSpecification(criteria);
        return saldoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Saldo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Saldo> findByCriteria(SaldoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Saldo> specification = createSpecification(criteria);
        return saldoRepository.findAll(specification, page);
    }

    /**
     * Function to convert SaldoCriteria to a {@link Specifications}
     */
    private Specifications<Saldo> createSpecification(SaldoCriteria criteria) {
        Specifications<Saldo> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Saldo_.id));
            }
            if (criteria.getMonto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonto(), Saldo_.monto));
            }
            if (criteria.getMoneda() != null) {
                specification = specification.and(buildSpecification(criteria.getMoneda(), Saldo_.moneda));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Saldo_.tipo));
            }
            if (criteria.getFechaCreacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCreacion(), Saldo_.fechaCreacion));
            }
            if (criteria.getFechaModificacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaModificacion(), Saldo_.fechaModificacion));
            }
            if (criteria.getUsuarioId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUsuarioId(), Saldo_.usuario, User_.id));
            }
            if (criteria.getAplicacionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAplicacionId(), Saldo_.aplicacion, App_.id));
            }
        }
        return specification;
    }

}
