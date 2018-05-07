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

import com.aantivero.paynow.domain.CargarSaldo;
import com.aantivero.paynow.domain.*; // for static metamodels
import com.aantivero.paynow.repository.CargarSaldoRepository;
import com.aantivero.paynow.repository.search.CargarSaldoSearchRepository;
import com.aantivero.paynow.service.dto.CargarSaldoCriteria;


/**
 * Service for executing complex queries for CargarSaldo entities in the database.
 * The main input is a {@link CargarSaldoCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CargarSaldo} or a {@link Page} of {@link CargarSaldo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CargarSaldoQueryService extends QueryService<CargarSaldo> {

    private final Logger log = LoggerFactory.getLogger(CargarSaldoQueryService.class);


    private final CargarSaldoRepository cargarSaldoRepository;

    private final CargarSaldoSearchRepository cargarSaldoSearchRepository;

    public CargarSaldoQueryService(CargarSaldoRepository cargarSaldoRepository, CargarSaldoSearchRepository cargarSaldoSearchRepository) {
        this.cargarSaldoRepository = cargarSaldoRepository;
        this.cargarSaldoSearchRepository = cargarSaldoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CargarSaldo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CargarSaldo> findByCriteria(CargarSaldoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<CargarSaldo> specification = createSpecification(criteria);
        return cargarSaldoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CargarSaldo} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CargarSaldo> findByCriteria(CargarSaldoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<CargarSaldo> specification = createSpecification(criteria);
        return cargarSaldoRepository.findAll(specification, page);
    }

    /**
     * Function to convert CargarSaldoCriteria to a {@link Specifications}
     */
    private Specifications<CargarSaldo> createSpecification(CargarSaldoCriteria criteria) {
        Specifications<CargarSaldo> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CargarSaldo_.id));
            }
            if (criteria.getMonto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonto(), CargarSaldo_.monto));
            }
            if (criteria.getUsuarioId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUsuarioId(), CargarSaldo_.usuario, User_.id));
            }
            if (criteria.getCuentaId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCuentaId(), CargarSaldo_.cuenta, Cuenta_.id));
            }
            if (criteria.getTransferenciaAppId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTransferenciaAppId(), CargarSaldo_.transferenciaApp, TransferenciaApp_.id));
            }
        }
        return specification;
    }

}
