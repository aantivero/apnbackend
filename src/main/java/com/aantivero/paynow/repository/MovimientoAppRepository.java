package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.MovimientoApp;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MovimientoApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovimientoAppRepository extends JpaRepository<MovimientoApp, Long>, JpaSpecificationExecutor<MovimientoApp> {

}
