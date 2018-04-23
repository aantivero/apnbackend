package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.CuentaApp;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CuentaApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuentaAppRepository extends JpaRepository<CuentaApp, Long> {

}
