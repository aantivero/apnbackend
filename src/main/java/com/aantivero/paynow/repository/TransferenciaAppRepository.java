package com.aantivero.paynow.repository;

import com.aantivero.paynow.domain.TransferenciaApp;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransferenciaApp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransferenciaAppRepository extends JpaRepository<TransferenciaApp, Long> {

}
