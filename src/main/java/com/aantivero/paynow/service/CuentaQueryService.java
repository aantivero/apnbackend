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

import com.aantivero.paynow.domain.Cuenta;
import com.aantivero.paynow.domain.*; // for static metamodels
import com.aantivero.paynow.repository.CuentaRepository;
import com.aantivero.paynow.repository.search.CuentaSearchRepository;
import com.aantivero.paynow.service.dto.CuentaCriteria;

import com.aantivero.paynow.domain.enumeration.Moneda;
import com.aantivero.paynow.domain.enumeration.TipoCuenta;

/**
 * Service for executing complex queries for Cuenta entities in the database.
 * The main input is a {@link CuentaCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cuenta} or a {@link Page} of {@link Cuenta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CuentaQueryService extends QueryService<Cuenta> {

    private final Logger log = LoggerFactory.getLogger(CuentaQueryService.class);


    private final CuentaRepository cuentaRepository;

    private final CuentaSearchRepository cuentaSearchRepository;

    public CuentaQueryService(CuentaRepository cuentaRepository, CuentaSearchRepository cuentaSearchRepository) {
        this.cuentaRepository = cuentaRepository;
        this.cuentaSearchRepository = cuentaSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Cuenta} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cuenta> findByCriteria(CuentaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Cuenta> specification = createSpecification(criteria);
        return cuentaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cuenta} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cuenta> findByCriteria(CuentaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Cuenta> specification = createSpecification(criteria);
        return cuentaRepository.findAll(specification, page);
    }

    /**
     * Function to convert CuentaCriteria to a {@link Specifications}
     */
    private Specifications<Cuenta> createSpecification(CuentaCriteria criteria) {
        Specifications<Cuenta> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Cuenta_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Cuenta_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Cuenta_.descripcion));
            }
            if (criteria.getMoneda() != null) {
                specification = specification.and(buildSpecification(criteria.getMoneda(), Cuenta_.moneda));
            }
            if (criteria.getSaldo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSaldo(), Cuenta_.saldo));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Cuenta_.tipo));
            }
            if (criteria.getCbu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCbu(), Cuenta_.cbu));
            }
            if (criteria.getAliasCbu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAliasCbu(), Cuenta_.aliasCbu));
            }
            if (criteria.getFechaCreacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCreacion(), Cuenta_.fechaCreacion));
            }
            if (criteria.getFechaModificacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaModificacion(), Cuenta_.fechaModificacion));
            }
            if (criteria.getHabilitada() != null) {
                specification = specification.and(buildSpecification(criteria.getHabilitada(), Cuenta_.habilitada));
            }
            if (criteria.getParaCredito() != null) {
                specification = specification.and(buildSpecification(criteria.getParaCredito(), Cuenta_.paraCredito));
            }
            if (criteria.getParaDebito() != null) {
                specification = specification.and(buildSpecification(criteria.getParaDebito(), Cuenta_.paraDebito));
            }
            if (criteria.getMaximoCredito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaximoCredito(), Cuenta_.maximoCredito));
            }
            if (criteria.getMaximoDebito() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaximoDebito(), Cuenta_.maximoDebito));
            }
            if (criteria.getCodigoSeguridad() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigoSeguridad(), Cuenta_.codigoSeguridad));
            }
            if (criteria.getUsuarioId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUsuarioId(), Cuenta_.usuario, User_.id));
            }
            if (criteria.getBancoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBancoId(), Cuenta_.banco, Banco_.id));
            }
        }
        return specification;
    }

}
