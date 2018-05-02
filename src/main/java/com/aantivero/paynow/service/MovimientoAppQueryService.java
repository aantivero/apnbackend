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

import com.aantivero.paynow.domain.MovimientoApp;
import com.aantivero.paynow.domain.*; // for static metamodels
import com.aantivero.paynow.repository.MovimientoAppRepository;
import com.aantivero.paynow.repository.search.MovimientoAppSearchRepository;
import com.aantivero.paynow.service.dto.MovimientoAppCriteria;

import com.aantivero.paynow.domain.enumeration.Moneda;

/**
 * Service for executing complex queries for MovimientoApp entities in the database.
 * The main input is a {@link MovimientoAppCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MovimientoApp} or a {@link Page} of {@link MovimientoApp} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MovimientoAppQueryService extends QueryService<MovimientoApp> {

    private final Logger log = LoggerFactory.getLogger(MovimientoAppQueryService.class);


    private final MovimientoAppRepository movimientoAppRepository;

    private final MovimientoAppSearchRepository movimientoAppSearchRepository;

    public MovimientoAppQueryService(MovimientoAppRepository movimientoAppRepository, MovimientoAppSearchRepository movimientoAppSearchRepository) {
        this.movimientoAppRepository = movimientoAppRepository;
        this.movimientoAppSearchRepository = movimientoAppSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MovimientoApp} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MovimientoApp> findByCriteria(MovimientoAppCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MovimientoApp> specification = createSpecification(criteria);
        return movimientoAppRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MovimientoApp} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MovimientoApp> findByCriteria(MovimientoAppCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MovimientoApp> specification = createSpecification(criteria);
        return movimientoAppRepository.findAll(specification, page);
    }

    /**
     * Function to convert MovimientoAppCriteria to a {@link Specifications}
     */
    private Specifications<MovimientoApp> createSpecification(MovimientoAppCriteria criteria) {
        Specifications<MovimientoApp> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MovimientoApp_.id));
            }
            if (criteria.getCuentaDebitoCbu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCuentaDebitoCbu(), MovimientoApp_.cuentaDebitoCbu));
            }
            if (criteria.getCuentaDebitoAlias() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCuentaDebitoAlias(), MovimientoApp_.cuentaDebitoAlias));
            }
            if (criteria.getCuentaDebitoPropia() != null) {
                specification = specification.and(buildSpecification(criteria.getCuentaDebitoPropia(), MovimientoApp_.cuentaDebitoPropia));
            }
            if (criteria.getCuentaCreditoCbu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCuentaCreditoCbu(), MovimientoApp_.cuentaCreditoCbu));
            }
            if (criteria.getCuentaCreditoAlias() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCuentaCreditoAlias(), MovimientoApp_.cuentaCreditoAlias));
            }
            if (criteria.getCuentaCreditoPropia() != null) {
                specification = specification.and(buildSpecification(criteria.getCuentaCreditoPropia(), MovimientoApp_.cuentaCreditoPropia));
            }
            if (criteria.getMoneda() != null) {
                specification = specification.and(buildSpecification(criteria.getMoneda(), MovimientoApp_.moneda));
            }
            if (criteria.getMonto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonto(), MovimientoApp_.monto));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), MovimientoApp_.timestamp));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), MovimientoApp_.descripcion));
            }
            if (criteria.getIdentificacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdentificacion(), MovimientoApp_.identificacion));
            }
            if (criteria.getConsolidado() != null) {
                specification = specification.and(buildSpecification(criteria.getConsolidado(), MovimientoApp_.consolidado));
            }
            if (criteria.getConsolidadoTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConsolidadoTimestamp(), MovimientoApp_.consolidadoTimestamp));
            }
            if (criteria.getBancoDebitoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBancoDebitoId(), MovimientoApp_.bancoDebito, Banco_.id));
            }
            if (criteria.getBancoCreditoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBancoCreditoId(), MovimientoApp_.bancoCredito, Banco_.id));
            }
        }
        return specification;
    }

}
